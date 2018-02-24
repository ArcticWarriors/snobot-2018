package com.snobot.vision_app.app2018.java_algorithm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.snobot.vision_app.app2018.FilterParams;
import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.VisionAlgorithmPreferences;
import com.snobot.vision_app.app2018.VisionRobotConnection;
import com.snobot.vision_app.app2018.detectors.CubeDetector;
import com.snobot.vision_app.app2018.detectors.IDetector;
import com.snobot.vision_app.app2018.detectors.PortalDetector;
import com.snobot.vision_app.app2018.detectors.TapeDetector;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PJ on 2/8/2018.
 */

public class JavaVisionAlgorithm
{
    private int mCameraDirection;
    private Map<CameraMode, IDetector> mDetectors;
    private Map<CameraMode, VisionAlgorithmPreferences> mDetectorsPreferences;
    private IDetector mActiveDetetor;

    private VisionRobotConnection mRobotConnection;
    private String mImagePrefix;
    private String mImageDumpDir;
    private boolean mRecordingImages;
    private boolean mRobotConnected;
    private DisplayType mDisplayType;
    private CameraMode mMode = CameraMode.SwitchTape;

    public JavaVisionAlgorithm(VisionRobotConnection aRobotConnection, Context aContext) {

        IDebugLogger debugLogger = new IDebugLogger() {
            @Override
            public void debug(Class<?> aClass, String aMessage) {
                Log.d(aClass.getName(), aMessage);
            }
        };

        mRobotConnection = aRobotConnection;

        mDetectors = new HashMap<>();
        mDetectors.put(CameraMode.SwitchTape, new TapeDetector(debugLogger));
        mDetectors.put(CameraMode.Cube, new CubeDetector(debugLogger));
        mDetectors.put(CameraMode.Exchange, new PortalDetector(debugLogger));

        mDetectorsPreferences = new HashMap<>();
        mDetectorsPreferences.put(CameraMode.SwitchTape, new VisionAlgorithmPreferences(CameraMode.SwitchTape.name(), aContext));
        mDetectorsPreferences.put(CameraMode.Cube, new VisionAlgorithmPreferences(CameraMode.Cube.name(), aContext));
        mDetectorsPreferences.put(CameraMode.Exchange, new VisionAlgorithmPreferences(CameraMode.Exchange.name(), aContext));

        mActiveDetetor = mDetectors.get(mMode);
    }

    public VisionAlgorithmPreferences getActivePreferences() {
        return mDetectorsPreferences.get(mMode);
    }

    public enum CameraMode
    {
        SwitchTape, Cube, Exchange
    }
    public void setDisplayType(DisplayType displayType) {
        mDisplayType = displayType;
    }

    public void setCameraDirection(int cameraDirection) {
        mCameraDirection = cameraDirection;
    }

    public void setRecording(boolean aRecord, String aName) {
        mRecordingImages = aRecord;
        mImagePrefix = aName;
    }

    public void iterateDisplayType() {
        int nextIndex = mDisplayType.ordinal() + 1;
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
        VisionAlgorithmPreferences preferences = getActivePreferences();
        Pair<Integer, Integer> filterWidth = preferences.getFilterWidthThreshold();
        Pair<Integer, Integer> filterHeight = preferences.getFilterHeightThreshold();
        Pair<Integer, Integer> filterVertices = preferences.getFilterVerticesThreshold();
        Pair<Float, Float> filterRatio = preferences.getFilterRatioRange();

        FilterParams filterParams = mActiveDetetor.getFilterParams();
        filterParams.mMinWidth = filterWidth.first;
        filterParams.mMaxWidth = filterWidth.second;
        filterParams.mMinHeight = filterHeight.first;
        filterParams.mMaxHeight = filterHeight.second;
        filterParams.mMinVertices = filterVertices.first;
        filterParams.mMaxVertices = filterVertices.second;
        filterParams.mMinRatio = filterRatio.first;
        filterParams.mMaxRatio = filterRatio.second;
        mActiveDetetor.setFilterParams(filterParams);

        Bitmap bitmap = Bitmap.createBitmap(aMat.cols(), aMat.rows(), Bitmap.Config.ARGB_8888);

        if(mRecordingImages && mRobotConnected) {
            writeImage(bitmap);
        }

        return mActiveDetetor.process(aMat, aSystemTimeNs);
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

        final File file = null; //new File(dir.getAbsolutePath(), mImagPrefix + "_" + curTime + ".jpg");

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

    public enum DisplayType {MarkedUpImage}
}
