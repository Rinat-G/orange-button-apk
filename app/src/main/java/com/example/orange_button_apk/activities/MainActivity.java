package com.example.orange_button_apk.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.example.orange_button_apk.HttpClientHandler;
import com.example.orange_button_apk.R;
import com.example.orange_button_apk.model.SessionCloseRequest;
import com.google.android.material.chip.Chip;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtplibrary.util.FpsListener;
import com.pedro.rtsp.rtsp.VideoCodec;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@AndroidEntryPoint
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
    @Inject
    public HttpClientHandler httpClientHandler;
    private RtspCamera1 rtspCamera1;
    private ImageView bStartStopStream;
    private Chip leftChip;
    private Chip rightChip;
    private String mSession;
    private String mToken;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }


        leftChip = findViewById(R.id.left_chip);
        leftChip.setText(R.string.left_chip_default_text);

        rightChip = findViewById(R.id.right_chip);
        rightChip.setText(getString(R.string.fps_chip_text, 0));

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(getSurfaceCallback());
        rtspCamera1 = new RtspCamera1(surfaceView, getConnectCheckerRtsp());
        rtspCamera1.setVideoCodec(VideoCodec.H264);
        rtspCamera1.setFpsListener(getFpsListener());
        mSession = getIntent().getStringExtra("session");

        initButtons();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.common_res_file), Context.MODE_PRIVATE);
        mToken = sharedPref.getString("token", null);
        getSupportActionBar().hide();
    }

    @NonNull
    private FpsListener.Callback getFpsListener() {
        return fps -> runOnUiThread(() -> rightChip.setText(getString(R.string.fps_chip_text, fps)));
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
                runOnUiThread(() -> leftChip.setText(bitrate / 1024 + " kbps"));
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
        bSwitchCamera.setOnClickListener(v -> rtspCamera1.switchCamera());

        bStartStopStream = findViewById(R.id.start_stop_stream_btn);
        bStartStopStream.setOnClickListener(v -> {
            if (!rtspCamera1.isStreaming() && prepareEncoders()) {
                rtspCamera1.startStream(getString(R.string.rtsp_url) + "/" + mSession);
                makeButtonActive(v);
            } else {
                showAlertDialog();
//                rtspCamera1.stopStream();
//                makeButtonInactive(v);
            }

        });

        ImageView bSettings = findViewById(R.id.settings_btn);
        bSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        builder.setTitle("Если с вами всё в порядке, введите пин-код")
                .setPositiveButton(
                        "Ок",
                        (dialogInterface, i) -> {
                            httpClientHandler.makeSessionCloseRequest(
                                    mToken,
                                    getSessionCloseCallback(),
                                    getString(R.string.base_url),
                                    new SessionCloseRequest(mSession, input.getText().toString()));
                        }
                )
                .setNegativeButton(
                        "Отмена",
                        (dialog, which) -> dialog.dismiss()
                )
                .setView(input)
                .create()
                .show();
    }

    private Callback getSessionCloseCallback() {
        return new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    rtspCamera1.stopStream();
                    runOnUiThread(() -> makeButtonInactive(bStartStopStream));
                }
            }
        };
    }

    private void makeButtonActive(View v) {
        ImageView iv = (ImageView) v;
        iv.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_camera_red));
    }

    private void makeButtonInactive(View v) {
        ImageView iv = (ImageView) v;
        iv.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_camera_orange));
        leftChip.setText(getString(R.string.left_chip_default_text));
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