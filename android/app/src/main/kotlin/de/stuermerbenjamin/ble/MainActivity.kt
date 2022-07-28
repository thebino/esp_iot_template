package de.stuermerbenjamin.ble

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val api31Permissions = arrayOf(BLUETOOTH_SCAN, BLUETOOTH_CONNECT)
            if (!hasPermissions(this, api31Permissions)) {
                requestPermissions(api31Permissions, 13)
            }
        } else {
            val api30Permissions = arrayOf(BLUETOOTH, BLUETOOTH_ADMIN, ACCESS_COARSE_LOCATION)
            if (!hasPermissions(this, api30Permissions)) {
                requestPermissions(api30Permissions, 13)
            }
        }

        setContent {
            val navController = rememberAnimatedNavController()
            AppNavGraph(navController = navController)
        }
    }

    private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}
