package com.zedfeorius.windroid.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;

public class RemoteControlService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 处理无障碍事件（如果需要）
    }

    @Override
    public void onInterrupt() {
        // 在服务中断时处理
    }

    public void performClick(float x, float y) {
        Path path = new Path();
        path.moveTo(x, y);

        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(path, 0, 100));
        dispatchGesture(gestureBuilder.build(), null, null);
    }



}