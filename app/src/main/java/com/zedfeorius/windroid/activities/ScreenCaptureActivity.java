package com.zedfeorius.windroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.zedfeorius.windroid.R;

public class ScreenCaptureActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> screenCaptureLauncher;


    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_capture);

        //初始化ActivityResultLauncher
        screenCaptureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        startScreenCapture(result.getResultCode(), result.getData());
                    }
                }
        );
        requestScreenCapture();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (virtualDisplay != null) {
            virtualDisplay.release();
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
    }


    private void requestScreenCapture(){
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
        screenCaptureLauncher.launch(captureIntent);
    }

    private void startScreenCapture(int resultCode, Intent data) {
        // 获取 MediaProjectionManager 实例
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        // 使用 resultCode 和 data 获取 MediaProjection
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);

        // 获取设备的实际屏幕分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        int screenDensity = metrics.densityDpi;

        // 设置用于显示屏幕捕获内容的 SurfaceView
        SurfaceView surfaceView = findViewById(R.id.surfaceView);  // 在布局中添加一个 SurfaceView
        // 这是接收屏幕内容的表面
        Surface surface = surfaceView.getHolder().getSurface();

        // 创建 VirtualDisplay 显示屏幕内容
        virtualDisplay = mediaProjection.createVirtualDisplay(
                "ScreenCapture",
                screenWidth, screenHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface, null, null
        );
    }



}