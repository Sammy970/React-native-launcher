package com.rntvlauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.rntvlauncher.MainActivity

class BootReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.let { action ->
            if (action == Intent.ACTION_BOOT_COMPLETED || 
                action == "android.intent.action.QUICKBOOT_POWERON") {
                    
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(launchIntent)

                // Launch an additional app after a delay to ensure the launcher is fully started
                // val additionalAppPackageName = "com.example.com" // Replace with the actual package name
                // val additionalAppIntent = context.packageManager.getLaunchIntentForPackage(additionalAppPackageName)?.apply {
                //     addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // }

                // additionalAppIntent?.let {
                //     // Delay the launch of the additional app to ensure the launcher is fully started
                //     val handler = android.os.Handler()
                //     handler.postDelayed({
                //         context.startActivity(it)
                //         Log.d(TAG, "Launched additional app: $additionalAppPackageName")
                //     }, 500) // Adjust the delay as needed
                // } ?: run {
                //     Log.e(TAG, "No launch intent found for additional app: $additionalAppPackageName")
                // }
            }
        }
    }
}
