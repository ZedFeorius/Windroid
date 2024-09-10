package com.zedfeorius.windroid.activities;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zedfeorius.windroid.R;
import com.zedfeorius.windroid.services.RemoteControlService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 检查并提示用户启用 AccessibilityService
        if (!isAccessibilityServiceEnabled(this)) {
            promptUserToEnableAccessibilityService();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取并设置按钮点击事件
        findViewById(R.id.btn_start_screen_capture).setOnClickListener(v -> {
            // 启动 ScreenCaptureActivity
            Intent intent = new Intent(MainActivity.this, ScreenCaptureActivity.class);
            startActivity(intent);
        });
    }

    private boolean isAccessibilityServiceEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo enabledService : enabledServices) {
            if (enabledService.getId().equals(context.getPackageName() + "/" + RemoteControlService.class.getName())) {
                return true;
            }
        }
        return false;
    }

    private void promptUserToEnableAccessibilityService() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "Please enable the accessibility service for remote control functionality.", Toast.LENGTH_LONG).show();
    }
}
