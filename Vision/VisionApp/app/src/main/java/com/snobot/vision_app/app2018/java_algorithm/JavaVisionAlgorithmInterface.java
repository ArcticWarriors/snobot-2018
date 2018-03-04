package com.snobot.vision_app.app2018.java_algorithm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.snobot.vision_app.app2018.CameraMode;
import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.VisionAlgorithm;
import com.snobot.vision_app.app2018.VisionAlgorithmPreferences;
import com.snobot.vision_app.app2018.VisionRobotConnection;
import com.snobot.vision_app.app2018.detectors.DisplayType;
import com.snobot.vision_app.app2018.detectors.IDetector;
import com.snobot.vision_app.app2018.detectors.ITensorFlowDetectorFactory;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PJ on 2/8/2018.
 */

public class JavaVisionAlgorithmInterface
{
    private int mCameraDirection;
    private Map<CameraMode, VisionAlgorithmPreferences> mDetectorsPreferences;

    private VisionRobotConnection mRobotConnection;
    private VisionAlgorithm<Bitmap> mVisionAlgorithm;
    private String mImagePrefix;
    private String mImageDumpDir;
    private boolean mRecordingImages;
    private boolean mRobotConnected;

    public JavaVisionAlgorithmInterface(VisionRobotConnection aRobotConnection, final Context aContext) {


        mImageDumpDir = "SnobotVision/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImagePrefix = "DefaultPrefix";

        mRobotConnection = aRobotConnection;

        mDetectorsPreferences = new HashMap<>();
        mDetectorsPreferences.put(CameraMode.SwitchTape, new VisionAlgorithmPreferences(CameraMode.SwitchTape.name(), aContext));
        mDetectorsPreferences.put(CameraMode.Cube, new VisionAlgorithmPreferences(CameraMode.Cube.name(), aContext));
        mDetectorsPreferences.put(CameraMode.Exchange, new VisionAlgorithmPreferences(CameraMode.Exchange.name(), aContext));


        IDebugLogger debugLogger = new IDebugLogger() {
            @Override
            public void debug(Class<?> aClass, String aMessage) {
                Log.d(aClass.getName(), aMessage);
            }
            @Override
            public void info(Class<?> aClass, String aMessage) {
                Log.i(aClass.getName(), aMessage);
            }
        };

        ITensorFlowDetectorFactory tfFactory = new ITensorFlowDetectorFactory() {
            @Override
            public IDetector createDetector(IDebugLogger aLogger) {
                TensorFlowDetector output = null;
                try
                {
                    output = new TensorFlowDetector(mRobotConnection, aContext.getAssets());
                }
                catch(Exception ex)
                {
                    Log.e(getClass().getName(), ex.toString());
                }
                return output;
            }
        };


        mVisionAlgorithm = new VisionAlgorithm(debugLogger, tfFactory);
        // TODO temp
        mVisionAlgorithm.setCameraMode(CameraMode.NoAlgorithm);
    }

    public VisionAlgorithmPreferences getActivePreferences() {
        return mDetectorsPreferences.get(mVisionAlgorithm.getCameraMode());
    }
    public void setDisplayType(DisplayType displayType) {
        mVisionAlgorithm.setDisplayType(displayType);
    }

    public void setCameraDirection(int cameraDirection) {
        mCameraDirection = cameraDirection;
    }

    public void setRecording(boolean aRecord, String aName) {
        mRecordingImages = aRecord;
        mImagePrefix = aName;
    }

    public void setRobotConnected(boolean robotConnected) {
        mRobotConnected = robotConnected;
    }

    public Mat processImage(Bitmap aBitmap, long aImageTimestamp) {
        Mat mat = new Mat();
        Utils.bitmapToMat(aBitmap, mat);

        return processImage(mat, aImageTimestamp);
    }

    public Mat processImage(Mat aMat, long aSystemTimeNs)
    {
//        VisionAlgorithmPreferences preferences = getActivePreferences();
//        Pair<Integer, Integer> filterWidth = preferences.getFilterWidthThreshold();
//        Pair<Integer, Integer> filterHeight = preferences.getFilterHeightThreshold();
//        Pair<Integer, Integer> filterVertices = preferences.getFilterVerticesThreshold();
//        Pair<Float, Float> filterRatio = preferences.getFilterRatioRange();
//
//        FilterParams filterParams = mActiveDetetor.getFilterParams();
//        filterParams.mMinWidth = filterWidth.first;
//        filterParams.mMaxWidth = filterWidth.second;
//        filterParams.mMinHeight = filterHeight.first;
//        filterParams.mMaxHeight = filterHeight.second;
//        filterParams.mMinVertices = filterVertices.first;
//        filterParams.mMaxVertices = filterVertices.second;
//        filterParams.mMinRatio = filterRatio.first;
//        filterParams.mMaxRatio = filterRatio.second;
//        mActiveDetetor.setFilterParams(filterParams);
//
        Bitmap bitmap = Bitmap.createBitmap(aMat.cols(), aMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(aMat, bitmap);

        if(true) {
            writeImage(bitmap);
        }
        return mVisionAlgorithm.processImage(bitmap, aMat, aSystemTimeNs);
    }

    private void writeImage(final Bitmap aImage)
    {
        long curTime = System.currentTimeMillis();

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File (root.getAbsolutePath() + "/" + mImageDumpDir);
        if(!dir.exists())
        {
            dir.mkdirs();
        }

        final File file = new File(dir.getAbsolutePath(), mImagePrefix + "_" + curTime + ".jpg");

        Runnable imageSaver = new Runnable() {
            @Override
            public void run() {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    aImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    System.out.println("Saved file!");
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(imageSaver).start();

    }
}
