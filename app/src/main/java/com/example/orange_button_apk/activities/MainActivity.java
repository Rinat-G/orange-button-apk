package com.example.orange_button_apk.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.example.orange_button_apk.R;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.rtsp.VideoCodec;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

public class MainActivity extends AppCompatActivity {
    public static final int VIDEO_RESOLUTION_WIDTH = 640;
    public static final int VIDEO_RESOLUTION_HEIGHT = 480;
    //    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
//    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
    public static final int VIDEO_BITRATE_KBPS = 2500;
    public static final int VIDEO_FPS = 30;
    public static final int AUDIO_BITRATE = 128;
    public static final int AUDIO_SAMPLE_RATE = 44100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private RtspCamera1 rtspCamera1;
    private ImageView bStartStopStream;
    private TextView tvBitrate;
    private String mSession;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }

        tvBitrate = findViewById(R.id.tvBitrate);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(getSurfaceCallback());
        rtspCamera1 = new RtspCamera1(surfaceView, getConnectCheckerRtsp());
        rtspCamera1.setVideoCodec(VideoCodec.H264);
//        rtspCamera1.setVideoCodec(VideoCodec.H265);

        mSession = getIntent().getStringExtra("session");

        initButtons();

    }

    private SurfaceHolder.Callback getSurfaceCallback() {
        return new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                rtspCamera1.startPreview();
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                if (rtspCamera1.isStreaming()) {
                    rtspCamera1.stopStream();
                    makeButtonInactive(bStartStopStream);
                }
                rtspCamera1.stopPreview();
            }
        };
    }

    private ConnectCheckerRtsp getConnectCheckerRtsp() {
        ConnectCheckerRtsp connectCheckerRtsp = new ConnectCheckerRtsp() {
            @Override
            public void onConnectionStartedRtsp(String s) {

            }

            @Override
            public void onConnectionSuccessRtsp() {

            }

            @Override
            public void onConnectionFailedRtsp(String s) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onNewBitrateRtsp(long bitrate) {
                runOnUiThread(() -> tvBitrate.setText(bitrate / 1024 + " kbps"));
            }

            @Override
            public void onDisconnectRtsp() {

            }

            @Override
            public void onAuthErrorRtsp() {

            }

            @Override
            public void onAuthSuccessRtsp() {

            }
        };
        return connectCheckerRtsp;
    }

    private void initButtons() {
        ImageView bSwitchCamera = findViewById(R.id.switch_camera_btn);
        bSwitchCamera.setOnClickListener(v -> {
            rtspCamera1.switchCamera();
        });

        bStartStopStream = findViewById(R.id.start_stop_stream_btn);
        bStartStopStream.setOnClickListener(v -> {
            if (!rtspCamera1.isStreaming() && prepareEncoders()) {
                rtspCamera1.startStream(getString(R.string.rtsp_url) + "/" + mSession);
                makeButtonActive(v);
            } else {
                rtspCamera1.stopStream();
                makeButtonInactive(v);
            }

        });
    }

    private void makeButtonActive(View v) {
        ImageView iv = (ImageView) v;
        iv.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_camera_red));
    }

    private void makeButtonInactive(View v) {
        ImageView iv = (ImageView) v;
        iv.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_camera_orange));
    }

    private boolean prepareEncoders() {
        return rtspCamera1.prepareVideo(
                VIDEO_RESOLUTION_WIDTH,
                VIDEO_RESOLUTION_HEIGHT,
                VIDEO_FPS,
                VIDEO_BITRATE_KBPS * 1024,
                CameraHelper.getCameraOrientation(this)
        ) &&
                rtspCamera1.prepareAudio(
                        AUDIO_BITRATE * 1024,
                        AUDIO_SAMPLE_RATE,
                        true
                );
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}