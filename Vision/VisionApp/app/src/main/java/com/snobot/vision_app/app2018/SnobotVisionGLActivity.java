package com.snobot.vision_app.app2018;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.snobot.vision_app.app2018.broadcastReceivers.RobotConnectionStateListener;
import com.snobot.vision_app.app2018.broadcastReceivers.RobotConnectionStatusBroadcastReceiver;
import com.snobot.vision_app.app2018.java_algorithm.JavaVisionAlgorithm;
import com.snobot.vision_app.utils.MjpgServer;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.opencv.android.OpenCVLoader;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SnobotVisionGLActivity extends Activity implements VisionRobotConnection.IVisionActivity, RobotConnectionStateListener {
    private static final String TAG = "CameraActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private VisionRobotConnection mRobotConnection;

    private SnobotVisionGLSurfaceView mView;
    private JavaVisionAlgorithm mAlgorithm;

    private VisionAlgorithmPreferences mPreferences;

    private RobotConnectionStatusBroadcastReceiver mRobotConnectionBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRobotConnection = new VisionRobotConnection(this);
        mRobotConnection.start();

        mPreferences = new VisionAlgorithmPreferences(this);
        mRobotConnectionBroadcastReceiver = new RobotConnectionStatusBroadcastReceiver(this, this);

        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }

        setContentView(R.layout.activity_snobot_vision_gl);

        openCamera();
    }
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        if (mView != null) {
            mView.onPause();
        }

        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.i("VisionActivity", "onResume " + mView);
        if (mView != null) {
            mView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRobotConnectionBroadcastReceiver);
    }

    private void openCamera() {
        MjpgServer.getInstance();

        // Add permission for camera and let user grant the permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SnobotVisionGLActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            return;
        }

        mAlgorithm = new JavaVisionAlgorithm(mRobotConnection, mPreferences);
        mAlgorithm.setDisplayType(JavaVisionAlgorithm.DisplayType.MarkedUpImage);

        mView = (SnobotVisionGLSurfaceView) findViewById(R.id.texture);
        mView.setCameraTextureListener(mView);
        mView.setVisionAlgorithm(mAlgorithm);
    }

    @Override
    public void useCamera(int aCameraId)
    {
        mAlgorithm.setCameraDirection(aCameraId);
        mView.setCameraIndex(aCameraId);
    }

    @SuppressWarnings("unchecked")
    public void openHsvSettingsSheet(View v)
    {
        final View view = getLayoutInflater().inflate(R.layout.hsl_settings, null);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.hsl_settings_window);
        container.getBackground().setAlpha(20);


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final RangeSeekBar<Integer> hueSeek = (RangeSeekBar<Integer>) view.findViewById(R.id.hueSeekBar);
        final RangeSeekBar<Integer> satSeek = (RangeSeekBar<Integer>) view.findViewById(R.id.satSeekBar);
        final RangeSeekBar<Integer> lumSeek = (RangeSeekBar<Integer>) view.findViewById(R.id.lumSeekBar);

        populateRangePair(hueSeek, mPreferences.getHueThreshold());
        populateRangePair(satSeek, mPreferences.getSatThreshold());
        populateRangePair(lumSeek, mPreferences.getLumThreshold());

        Button restoreButton = (Button) view.findViewById(R.id.restoreAlgorithimDefaultsButton);
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.restoreHslDefaults();
                dialog.dismiss();
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.saveAlgorithmSettingsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.setHueThreshold(getRangePair(hueSeek));
                mPreferences.setSatThreshold(getRangePair(satSeek));
                mPreferences.setLumThreshold(getRangePair(lumSeek));
                dialog.dismiss();
            }
        });
    }

    public void openFilterSettingsBottomSheet(View v)
    {
        final View view = getLayoutInflater().inflate(R.layout.filter_settings, null);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.filter_settings_window);
        container.getBackground().setAlpha(20);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final RangeSeekBar<Integer> widthSeek = (RangeSeekBar<Integer>) view.findViewById(R.id.width_settings);
        final RangeSeekBar<Integer> heightSeek = (RangeSeekBar<Integer>) view.findViewById(R.id.height_settings);
        final RangeSeekBar<Integer> verticesSeek = (RangeSeekBar<Integer>) view.findViewById(R.id.num_vertices_settings);
        final RangeSeekBar<Float> ratioSeek = (RangeSeekBar<Float>) view.findViewById(R.id.ratio_settings);

        populateRangePair(widthSeek, mPreferences.getFilterWidthThreshold());
        populateRangePair(heightSeek, mPreferences.getFilterHeightThreshold());
        populateRangePair(verticesSeek, mPreferences.getFilterVerticesThreshold());
        populateRangePair(ratioSeek, mPreferences.getFilterRatioRange());

        Button restoreButton = (Button) view.findViewById(R.id.restore_filter_settings);
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.restoreFilterDefaults();
                dialog.dismiss();
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.save_filter_settings);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.setFilterWidthRange(getRangePair(widthSeek));
                mPreferences.setFilterHeightRange(getRangePair(heightSeek));
                mPreferences.setFilterVerticesRange(getRangePair(verticesSeek));
                mPreferences.setFilterRatioRange(getRangePair(ratioSeek));
                dialog.dismiss();
            }
        });
    }

    private <T extends Number> Pair<T, T> getRangePair(RangeSeekBar<T> aRangeBar)
    {
        return new Pair<>(aRangeBar.getSelectedMinValue(), aRangeBar.getSelectedMaxValue());
    }

    private <T extends Number> void populateRangePair(RangeSeekBar<T> aRangeBar, Pair<T, T> aThreshold)
    {
        aRangeBar.setSelectedMinValue(aThreshold.first);
        aRangeBar.setSelectedMaxValue(aThreshold.second);
    }

    @Override
    public void iterateDisplayType() {
        mAlgorithm.iterateDisplayType();
    }

    @Override
    public void setRecording(final boolean aRecord, final String aName) {
        mAlgorithm.setRecording(aRecord, aName);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String toast = aRecord ? "Recording Images" : "Not Recording Images";
                Toast.makeText(SnobotVisionGLActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Attempt at using Volume Buttons to take pictures.
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                Log.i("Debug1", "Reading the volume UP button");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.i("Debug3", "Reading the volume DOWN button");
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void robotConnected() {
        View  connectionStateView = findViewById(R.id.connectionState);
        Toast.makeText(this, "Connected to Robot", Toast.LENGTH_SHORT).show();
        connectionStateView.setBackgroundColor(ContextCompat.getColor(this, R.color.app_connected));
        mAlgorithm.setRobotConnected(true);
    }

    @Override
    public void robotDisconnected() {
        View  connectionStateView = findViewById(R.id.connectionState);
        Toast.makeText(this, "Lost connection to Robot", Toast.LENGTH_SHORT).show();
        connectionStateView.setBackgroundColor(ContextCompat.getColor(this, R.color.app_disconnected));
        mAlgorithm.setRobotConnected(false);
    }
}
