package org.snobot.vision_tester.java_algorithm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.opencv.core.Mat;
import org.snobot.vision_tester.utils.OpenCvUtilities;
import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Session.Runner;
import org.tensorflow.Tensor;
import org.tensorflow.types.UInt8;

import com.snobot.vision_app.app2018.detectors.tf.Detection;
import com.snobot.vision_app.app2018.detectors.tf.MachineLearningDetector;

public class TensorflowAlgorithm extends MachineLearningDetector<BufferedImage>
{
    private final Session mSession;

    /**
     * Constructor. Used for the standalone wrapper of the ML algorithm
     * 
     * @param aGraphFile
     *            The path to the file used to configure the TF graph
     * @throws IOException
     *             Exception if the files could not be read
     */
    public TensorflowAlgorithm(String aGraphFile, String aLabelsFile) throws IOException
    {
        super(new FileInputStream(aLabelsFile));
        Graph graph = new Graph();

        try
        {
            graph.importGraphDef(Files.readAllBytes(new File(aGraphFile).toPath()));
        }
        catch (IOException ex)
        {
            LogManager.getLogger(TensorflowAlgorithm.class).error(ex);
        }

        mSession = new Session(graph);

        validateGraph(graph);
    }

    @SuppressWarnings("unchecked")
    private void runProcessing(Tensor<?> aImage)
    {
        Runner runner = mSession.runner().feed("image_tensor:0", aImage);
        for (String key : super.mOutputNames)
        {
            runner.fetch(key);
        }

        List<Tensor<?>> results = runner.run();
        Tensor<Float> boxes = (Tensor<Float>) results.get(0);
        Tensor<Float> scores = (Tensor<Float>) results.get(1);
        Tensor<Float> classes = (Tensor<Float>) results.get(2);
        // Tensor<Float> num_dets = (Tensor<Float>) results.get(3);

        populateBoxes(boxes);
        populateSimpleArray(scores, mOutputScores);
        populateSimpleArray(classes, mOutputClasses);
    }

    private void populateBoxes(Tensor<Float> aResult)
    {
        final long[] shape = aResult.shape();
        float[][][] stuff = new float[(int) shape[0]][(int) shape[1]][(int) shape[2]];
        aResult.copyTo(stuff);

        int ctr = 0;

        for (int i = 0; i < stuff.length; ++i)
        {

            for (int j = 0; j < stuff[i].length; ++j)
            {
                // System.out.println(Arrays.toString(stuff[i][j]));
                for (int k = 0; k < stuff[i][j].length; ++k)
                {
                    mOutputLocations[ctr++] = stuff[i][j][k];
                }
            }
        }
    }

    private void populateSimpleArray(Tensor<Float> aResult, float[] aOutput) // NOPMD
    {
        final long[] shape = aResult.shape();
        float[][] stuff = new float[(int) shape[0]][(int) shape[1]];
        aResult.copyTo(stuff);

        System.arraycopy(stuff[0], 0, aOutput, 0, stuff[0].length);
    }

    private static Tensor<?> constructAndExecuteGraphToNormalizeImage(byte[] aImageBytes)
    {
        try (Graph g = new Graph())
        {
            GraphBuilder b = new GraphBuilder(g);

            final Output<String> input = b.constant("input", aImageBytes);
            final Output<Float> output = 
                                            b.expandDims(
                                                    b.cast("Cast", b.decodeJpeg(input, 3), Float.class), 
                                                    b.constant("make_batch", 0));

            final Output<UInt8> castOutput = b.cast("CastBack", output, UInt8.class);
            try (Session s = new Session(g))
            {
                return s.runner().fetch(castOutput.op().name()).run().get(0).expect(UInt8.class);
            }
        }
    }

    private Tensor<?> convert(Mat aOriginalImage)
    {
        Tensor<?> output = null;
        try
        {
            BufferedImage bi = OpenCvUtilities.matToImage(aOriginalImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            baos.flush();

            output = constructAndExecuteGraphToNormalizeImage(baos.toByteArray());
        }
        catch (Exception ex)
        {
            LogManager.getLogger(TensorflowAlgorithm.class).error(ex);
        }

        return output;
    }

    @Override
    public Mat process(BufferedImage aRawImage, Mat aOriginalImage, long aSystemTimeNs)
    {
        Tensor<?> tensorImage = convert(aOriginalImage);
        runProcessing(tensorImage);

        List<Detection> boxes = processResults(aOriginalImage.cols(), aOriginalImage.rows());

        return markUpImage(aOriginalImage, boxes);
    }

    private static class GraphBuilder
    {
        private final Graph mGraph;

        private GraphBuilder(Graph aGraph)
        {
            this.mGraph = aGraph;
        }

        <T> Output<T> constant(String aName, Object aValue, Class<T> aType)
        {
            try (Tensor<T> t = Tensor.<T>create(aValue, aType))
            {
                return mGraph.opBuilder("Const", aName).setAttr("dtype", DataType.fromClass(aType)).setAttr("value", t).build().<T>output(0);
            }
        }

        Output<String> constant(String aName, byte[] aValue)
        {
            return this.constant(aName, aValue, String.class);
        }

        Output<Integer> constant(String aName, int aValue)
        {
            return this.constant(aName, aValue, Integer.class);
        }

        <T> Output<T> expandDims(Output<T> aInput, Output<Integer> aDim)
        {
            return binaryOp3("ExpandDims", aInput, aDim);
        }

        Output<UInt8> decodeJpeg(Output<String> aContents, long aChannels)
        {
            return mGraph.opBuilder("DecodeJpeg", "DecodeJpeg").addInput(aContents).setAttr("channels", aChannels).build().<UInt8>output(0);
        }

        <T, U> Output<U> cast(String aNodeName, Output<T> aValue, Class<U> aType)
        {
            DataType dtype = DataType.fromClass(aType);
            return mGraph.opBuilder("Cast", aNodeName).addInput(aValue).setAttr("DstT", dtype).build().<U>output(0);
        }

        <T, U, V> Output<T> binaryOp3(String aType, Output<U> aIn1, Output<V> aIn2)
        {
            return mGraph.opBuilder(aType, aType).addInput(aIn1).addInput(aIn2).build().<T>output(0);
        }
    }
}
