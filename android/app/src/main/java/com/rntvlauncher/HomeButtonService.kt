package com.rntvlauncher

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log

class HomeButtonService : AccessibilityService() {

    companion object {
        private const val TAG = "HomeButtonService"
    }

    override fun onServiceConnected() {
        val info = serviceInfo
        info.flags = info.flags or AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
        serviceInfo = info
        Log.d(TAG, "Service connected with flags: ${info.flags}")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            Log.d(TAG, "Accessibility event received: ${event.eventType}, package: ${event.packageName}")

            if (event.packageName == "com.android.systemui" && 
                (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || 
                 event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)) {
                Log.d(TAG, "Intercepting system UI event")
                performGlobalAction(GLOBAL_ACTION_HOME)
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        event?.let {
            Log.d(TAG, "Key event received: ${it.keyCode}, action: ${it.action}")

            when (it.keyCode) {
                // if keycode is 83 then don't do anything
                83 -> {
                    // log some unique event
                    Log.d(TAG, "Key code 83 pressed")
                    // block the event
                    return true
                }
                172 -> {
                    Log.d(TAG, "Key code 172 pressed")
                    return true
                }
                288 -> {
                    Log.d(TAG, "Key code 288 pressed")
                    return true
                }
                191 -> {
                    Log.d(TAG, "Key code 191 pressed")
                    return true
                }
                409 -> {
                    Log.d(TAG, "Key code 409 pressed")
                    return true
                }
                291 -> {
                    Log.d(TAG, "Key code 291 pressed")
                    return true
                }
                433 -> {
                    Log.d(TAG, "Key code 433 pressed")
                    return true
                }
                KeyEvent.KEYCODE_HOME -> {
                    if (it.action == KeyEvent.ACTION_UP) {
                        Log.d(TAG, "Home button pressed")
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        return true
                    }
                }
                KeyEvent.KEYCODE_BACK -> {
                    val currentPackage = rootInActiveWindow?.packageName
                    Log.d(TAG, "Back button pressed, current package: $currentPackage")
                    if (currentPackage?.toString() == "com.rntvlauncher") {
                        return true
                    }
                }
                KeyEvent.KEYCODE_APP_SWITCH -> {
                    Log.d(TAG, "App switch button pressed")
                    return true
                }
            }
        }
        return super.onKeyEvent(event)
    }
}