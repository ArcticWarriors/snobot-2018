package com.snobot.vision_app.app2018.java_algorithm;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.snobot.vision_app.app2018.VisionRobotConnection;
import com.snobot.vision_app.app2018.detectors.tf.MachineLearningDetector;
import com.snobot.vision_app.app2018.detectors.tf.Detection;
import com.snobot.vision_app.app2018.messages.TargetUpdateMessage;

import org.opencv.core.Mat;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pj on 2/25/18.
 */

public class TensorFlowDetector extends MachineLearningDetector<Bitmap>
{
    private static final String sASSETS_LABEL_LIST = "aw_power_up_inference_labels.txt";
    private static final String sASSETS_MODEL_FILENAME = "file:///android_asset/aw_power_up_inference_graph.pb";
//    private static final String sASSETS_LABEL_LIST = "tf_demo_labels.txt";
//    private static final String sASSETS_MODEL_FILENAME = "file:///android_asset/tf_demo_graph.pb";
    private static final int sINPUT_SIZE = 300;

    private final TensorFlowInferenceInterface mTfInterface;

    private int[] intValues;
    private byte[] byteValues;

    private VisionRobotConnection mRobotConnection;

    public TensorFlowDetector(VisionRobotConnection aRobotConnection, AssetManager aAssetManager) throws IOException {

        super(aAssetManager.open(sASSETS_LABEL_LIST));

        mRobotConnection = aRobotConnection;

        mTfInterface = new TensorFlowInferenceInterface(aAssetManager, sASSETS_MODEL_FILENAME);
        validateGraph(mTfInterface.graph());

        intValues = new int[sINPUT_SIZE * sINPUT_SIZE];
        byteValues = new byte[sINPUT_SIZE * sINPUT_SIZE * 3];
    }

    @Override
    public Mat process(Bitmap aBitmap, Mat aOriginalImage, long aSystemTimeNs)
    {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(aBitmap, sINPUT_SIZE, sINPUT_SIZE, false);

        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.getWidth(), 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());

        for (int i = 0; i < intValues.length; ++i) {
            byteValues[i * 3 + 2] = (byte) (intValues[i] & 0xFF);
            byteValues[i * 3 + 1] = (byte) ((intValues[i] >> 8) & 0xFF);
            byteValues[i * 3 + 0] = (byte) ((intValues[i] >> 16) & 0xFF);
        }

        mTfInterface.feed(mInputName, byteValues, 1, sINPUT_SIZE, sINPUT_SIZE, 3);
        mTfInterface.run(mOutputNames, false);

        mTfInterface.fetch(mOutputNames[0], mOutputLocations);
        mTfInterface.fetch(mOutputNames[1], mOutputScores);
        mTfInterface.fetch(mOutputNames[2], mOutputClasses);
        mTfInterface.fetch(mOutputNames[3], mOutputNumDetections);

        List<Detection> boxes = processResults(aOriginalImage.cols(), aOriginalImage.rows());

        sendTargetInformation(aSystemTimeNs, boxes);

        return markUpImage(aOriginalImage, boxes);
    }

    protected void sendTargetInformation(double aSystemTimeNs, List<Detection> boxes)
    {
        long currentTime = System.nanoTime();
        double latencySec = (currentTime - aSystemTimeNs) / 1e9;
        List<TargetUpdateMessage.TargetInfo> targets = new ArrayList<>();

        for(Detection detection : boxes)
        {
            targets.add(new TargetUpdateMessage.TargetInfo(detection.mClassificationName, detection.mAngle, detection.mDistanceFromHoriz, false));
        }

        mRobotConnection.sendVisionUpdate(targets, latencySec);
    }
}
