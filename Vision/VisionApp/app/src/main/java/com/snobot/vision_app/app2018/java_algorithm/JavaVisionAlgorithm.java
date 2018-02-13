package com.snobot.vision_app.app2018.java_algorithm;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.VisionAlgorithmPreferences;
import com.snobot.vision_app.app2018.VisionRobotConnection;
import com.snobot.vision_app.app2018.detectors.CubeDetector;
import com.snobot.vision_app.app2018.FilterParams;
import com.snobot.vision_app.app2018.detectors.IDetector;
import com.snobot.vision_app.app2018.detectors.PortalDetector;
import com.snobot.vision_app.app2018.detectors.TapeDetector;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by PJ on 2/8/2018.
 */

public class JavaVisionAlgorithm
{
    private int mCameraDirection;
    private VisionAlgorithmPreferences mPreferences;
    private IDetector mTapeDetector;
    private IDetector mCubeDetector;
    private IDetector mPortalDetector;

    private VisionRobotConnection mRobotConnection;
    private String mImagePrefix;
    private String mImageDumpDir;
    private boolean mRecordingImages;
    private boolean mRobotConnected;
    private DisplayType mDisplayType;
    private FilterParams mFilterParams;
    private cameraMode mMode = cameraMode.SwitchTape;

    public JavaVisionAlgorithm(VisionRobotConnection aRobotConnection, VisionAlgorithmPreferences aPreferences) {

        IDebugLogger debugLogger = new IDebugLogger() {
            @Override
            public void debug(String aMessage) {
                Log.d(getClass().getName(), aMessage);
            }
        };

        mPreferences = aPreferences;
        mRobotConnection = aRobotConnection;
        mTapeDetector = new TapeDetector(debugLogger);
        mCubeDetector =  new CubeDetector(debugLogger);
        mPortalDetector = new PortalDetector(debugLogger);
    }

    public enum cameraMode
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
        Pair<Integer, Integer> hue = mPreferences.getHueThreshold();
        Pair<Integer, Integer> sat = mPreferences.getSatThreshold();
        Pair<Integer, Integer> lum = mPreferences.getLumThreshold();

        Pair<Integer, Integer> filterWidth = mPreferences.getFilterWidthThreshold();
        Pair<Integer, Integer> filterHeight = mPreferences.getFilterHeightThreshold();
        Pair<Integer, Integer> filterVertices = mPreferences.getFilterVerticesThreshold();
        Pair<Float, Float> filterRatio = mPreferences.getFilterRatioRange();

        mFilterParams.mMinWidth = filterWidth.first;
        mFilterParams.mMaxWidth = filterWidth.second;
        mFilterParams.mMinHeight = filterHeight.first;
        mFilterParams.mMaxHeight = filterHeight.second;
        mFilterParams.mMinVertices = filterVertices.first;
        mFilterParams.mMaxVertices = filterVertices.second;
        mFilterParams.mMinRatio = filterRatio.first;
        mFilterParams.mMaxRatio = filterRatio.second;

        Bitmap bitmap = Bitmap.createBitmap(aMat.cols(), aMat.rows(), Bitmap.Config.ARGB_8888);

        if(mRecordingImages && mRobotConnected) {
            writeImage(bitmap);
        }

        if(mMode == cameraMode.SwitchTape) {
            return mTapeDetector.process(aMat, aSystemTimeNs);
        }
        else if(mMode == cameraMode.Cube)
        {
            return mCubeDetector.process(aMat, aSystemTimeNs);
        }
        else if(mMode == cameraMode.Exchange)
        {
            return mPortalDetector.process(aMat, aSystemTimeNs);
        }
        return null;
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
