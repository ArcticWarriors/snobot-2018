package com.snobot.vision_app.app2018.detectors.tf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.Graph;
import org.tensorflow.Operation;

import com.snobot.vision_app.app2018.FilterParams;
import com.snobot.vision_app.app2018.TargetLocation;
import com.snobot.vision_app.app2018.detectors.DisplayType;
import com.snobot.vision_app.app2018.detectors.DistanceCalculationUtility;
import com.snobot.vision_app.app2018.detectors.IDetector;
import com.snobot.vision_app.app2018.detectors.tf.Detection.BoudingBox;

public abstract class MachineLearningDetector<RawImageType> implements IDetector<RawImageType>
{
    private static final int MAX_RESULTS = 100;

    protected static final DecimalFormat sDF = new DecimalFormat("0.00");
    protected static final Scalar sCENTER_LINE_COLOR = new Scalar(0, 255, 0);
    protected static final Scalar sBLACK_COLOR = new Scalar(0, 0, 0);
    protected static final Scalar sWHITE_COLOR = new Scalar(255, 255, 255);

    protected static final Scalar[] sCONTOUR_COLORS = new Scalar[]
    {
        new Scalar(0, 0, 255),
        new Scalar(255, 0, 0),
        new Scalar(0, 255, 0)
    };

    protected final String mInputName;

    protected final float[] mOutputLocations;
    protected final float[] mOutputScores;
    protected final float[] mOutputClasses;
    protected final float[] mOutputNumDetections;
    protected final String[] mOutputNames;
    protected final double mMinConfidence;
    protected final List<String> mLabels;
    protected final Map<String, Scalar> mObjectSizes;

    /**
     * Constructor.
     * 
     * @throws IOException
     *             Throws an exception if the stream cannot be read
     */
    public MachineLearningDetector(InputStream aLabelStream) throws IOException
    {
        mInputName = "image_tensor";
        mOutputScores = new float[MAX_RESULTS];
        mOutputLocations = new float[MAX_RESULTS * 4];
        mOutputClasses = new float[MAX_RESULTS];
        mOutputNumDetections = new float[1];
        mOutputNames = new String[] {"detection_boxes", "detection_scores", "detection_classes", "num_detections"};

        mMinConfidence = .50;
        mLabels = new ArrayList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(aLabelStream));
        String line;
        while ((line = br.readLine()) != null)
        {
            mLabels.add(line);
        }
        br.close();

        mObjectSizes = new HashMap<>();
        mObjectSizes.put("coke", new Scalar(2.60, 4.83));
        mObjectSizes.put("coke_bottle", new Scalar(6, 12));
        mObjectSizes.put("cube", new Scalar(13, 11));
    }

    protected List<Detection> processResults(int aImageWidth, int aImageHeight)
    {
        PriorityQueue<Detection> pq = new PriorityQueue<Detection>(mOutputScores.length, mTargetComparator);

        for (int i = 0; i < mOutputScores.length; ++i)
        {
            float confidence = mOutputScores[i];
            if (confidence < mMinConfidence)
            {
                continue;
            }

            final BoudingBox detection = new BoudingBox(
                    mOutputLocations[4 * i + 0] * aImageHeight,
                    mOutputLocations[4 * i + 1] * aImageWidth,
                    mOutputLocations[4 * i + 2] * aImageHeight,
                    mOutputLocations[4 * i + 3] * aImageWidth);
            
            String label = mLabels.get((int) mOutputClasses[i]);
            Scalar objectSize = mObjectSizes.get(label);
            if (objectSize == null)
            {
                System.err.println("Unknown label " + label); // NOPMD
                objectSize = new Scalar(0, 0, 0);
            }

            TargetLocation location = DistanceCalculationUtility.calculate(
                    objectSize.val[0], objectSize.val[1],
                    detection.getRight() - detection.getLeft(), 
                    detection.getBottom() - detection.getTop(), 
                    detection.getLeft(),
                    null);
            
            pq.add(new Detection(label, confidence, detection,
                    location.getAngle(), location.getDistanceFromHoriz(), location.getDistanceFromVert(), location.getAspectRatio()));
        }

        ArrayList<Detection> recognitions = new ArrayList<Detection>(pq.size());
        int max = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < max; ++i)
        {
            recognitions.add(pq.poll());
        }

        return recognitions;
    }

    protected Mat markUpImage(Mat aOriginal, List<Detection> aTargets)
    {
        Mat displayImage = new Mat();
        aOriginal.copyTo(displayImage);

        int ctr = 0;
        for (Detection target : aTargets)
        {
            Point[] array = new Point[4];
            BoudingBox box = target.mBoundingBox;

            ++ctr;

            double normalizedBottom = box.getBottom();
            double normalizedTop = box.getTop();
            double normalizedRight = box.getRight();
            double normalizedLeft = box.getLeft();

            array[0] = new Point(normalizedLeft, normalizedTop);
            array[1] = new Point(normalizedLeft, normalizedBottom);
            array[2] = new Point(normalizedRight, normalizedBottom);
            array[3] = new Point(normalizedRight, normalizedTop);
            MatOfPoint boxPoints = new MatOfPoint(array);

            Scalar contourColor = sCONTOUR_COLORS[ctr % sCONTOUR_COLORS.length];
            Imgproc.drawContours(displayImage, Arrays.asList(boxPoints), 0, contourColor, 3);

            // Draw text
            String label = target.mClassificationName + "   " + sDF.format(target.mConfidence * 100);
            int fontFace = Core.FONT_HERSHEY_PLAIN;
            double fontScale = .6;

            Size fontSize = Imgproc.getTextSize(label, fontFace, fontScale, 1, null);
            Point rectTopLeft = new Point(normalizedLeft, normalizedTop + fontSize.height);
            Point rectBotRight = new Point(normalizedLeft + fontSize.width, normalizedTop);
            Point textStart = new Point(normalizedLeft, normalizedTop + fontSize.height);

            Imgproc.rectangle(displayImage, rectTopLeft, rectBotRight, contourColor, -1);
            Imgproc.putText(displayImage, label, textStart, fontFace, fontScale, sWHITE_COLOR);
        }

        return displayImage;
    }

    protected final void validateGraph(Graph aGraph)
    {
        final Operation inputOp = aGraph.operation(mInputName);
        if (inputOp == null)
        {
            throw new RuntimeException("Failed to find input Node '" + mInputName + "'");
        }

        // The outputScoresName node has a shape of [N, NumLocations], where N
        // is the batch size.
        final Operation outputOp1 = aGraph.operation("detection_scores");
        if (outputOp1 == null)
        {
            throw new RuntimeException("Failed to find output Node 'detection_scores'");
        }
        final Operation outputOp2 = aGraph.operation("detection_boxes");
        if (outputOp2 == null)
        {
            throw new RuntimeException("Failed to find output Node 'detection_boxes'");
        }
        final Operation outputOp3 = aGraph.operation("detection_classes");
        if (outputOp3 == null)
        {
            throw new RuntimeException("Failed to find output Node 'detection_classes'");
        }
    }

    private final Comparator<Detection> mTargetComparator = new Comparator<Detection>()
    {

        @Override
        public int compare(Detection aLeft, Detection aRight)
        {
            return Float.compare(aRight.mConfidence, aLeft.mConfidence);
        }
    };

    @Override
    public void setFilterParams(FilterParams aFilterParams) // NOPMD
    {
        // Not used
    }

    @Override
    public void setDisplayType(DisplayType aDisplayType) // NOPMD
    {
        // Not used
    }

    @Override
    public FilterParams getFilterParams() // NOPMD
    {
        return null;
    }

}
