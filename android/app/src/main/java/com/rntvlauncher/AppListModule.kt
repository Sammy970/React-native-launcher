package com.rntvlauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import android.util.Log

@ReactModule(name = "AppListModule")
class AppListModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    companion object {
        private const val TAG = "AppListModule"
    }

    override fun getName() = "AppListModule"

    @ReactMethod
    fun getInstalledApps(promise: Promise) {
        try {
            Log.d(TAG, "Starting to fetch installed apps")
            val pm = reactApplicationContext.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            
            val resolveInfos = pm.queryIntentActivities(intent, 0)
            val apps = Arguments.createArray()

            resolveInfos.forEach { resolveInfo ->
                val packageName = resolveInfo.activityInfo.packageName
                val appInfo = try {
                    pm.getApplicationInfo(packageName, 0)
                } catch (e: Exception) {
                    null
                }

                appInfo?.let {
                    // Include all apps that have a launcher intent
                    val appMap = Arguments.createMap().apply {
                        putString("packageName", packageName)
                        putString("name", pm.getApplicationLabel(it).toString())
                        
                        // Get the app icon and convert it to a Base64 string
                        val icon = pm.getApplicationIcon(it)
                        val bitmap = getBitmapFromDrawable(icon)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()
                        val encodedIcon = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        putString("icon", encodedIcon)
                    }
                    apps.pushMap(appMap)
                }
            }

            Log.d(TAG, "Found ${apps.size()} apps")
            promise.resolve(apps)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting installed apps", e)
            promise.reject("ERROR", e.message)
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is AdaptiveIconDrawable -> {
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
            else -> throw IllegalArgumentException("Unsupported drawable type")
        }
    }

    @ReactMethod
    fun launchApp(packageName: String, promise: Promise) {
        try {
            Log.d(TAG, "Attempting to launch app: $packageName")
            val pm = reactApplicationContext.packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            if (intent != null) {
                Log.d(TAG, "Found launch intent for $packageName")
                reactApplicationContext.startActivity(intent)
                Log.d(TAG, "Successfully launched $packageName")
                promise.resolve(true)
            } else {
                Log.e(TAG, "No launch intent found for $packageName")
                promise.reject("ERROR", "No launch intent found for $packageName")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching app: $packageName", e)
            promise.reject("ERROR", e.message)
        }
    }
}