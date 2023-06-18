package com.gozipp.zumer

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.R
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.gozipp.zumer.databinding.ActivityLocationEnableBinding
import com.gozipp.zumer.utills.Constant.LOCATION_CHECK
import com.gozipp.zumer.utills.PreferenceHelper
import java.util.*


class LocationEnableActivity : AppCompatActivity() {

    val FINE_LOCATION_RO = 101
    private val REQUEST_ENABLE_GPS = 1


    private var locationManager: LocationManager? = null

    private val REQUEST_CHECK_SETTINGS = 0x1
    private lateinit var binding: ActivityLocationEnableBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationEnableBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // enableGps()
        buttonTaps()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun buttonTaps() {
        binding.btnAllowPermission.setOnClickListener {


             checkForPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                "location",
                FINE_LOCATION_RO
            )

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            PreferenceHelper.writeBooleanToPreference(LOCATION_CHECK, false)
            // Permission is already granted
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            if (shouldShowRequestPermissionRationale(permission)) {
                // Show rationale dialog
                showDialog(permission, name, requestCode)
            } else {
                // Request permission directly
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } else {
                PreferenceHelper.writeBooleanToPreference(LOCATION_CHECK, false)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        when (requestCode) {
            FINE_LOCATION_RO -> innerCheck("location")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("permission to access your $name is requested to use this app")
            setTitle("permission requested")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@LocationEnableActivity,
                    arrayOf(permission), requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_GPS && resultCode == Activity.RESULT_OK) {
            PreferenceHelper.writeBooleanToPreference(LOCATION_CHECK, false)
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun enableGps() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                // For Android 6.0 and above, use the Settings API
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, REQUEST_ENABLE_GPS)
                }
                else {
                    // For earlier Android versions, try to enable GPS using different methods

                    // Method 1: Enable GPS using the Secure settings
                    try {
                        val secureSettingsClass = Settings.Secure::class.java
                        val secureSettingsMethod = secureSettingsClass.getMethod(
                            "putInt",
                            ContentResolver::class.java,
                            String::class.java,
                            Int::class.javaPrimitiveType
                        )

                        val contentResolver = applicationContext.contentResolver
                        val locationMode: Int = Settings.Secure.getInt(
                            contentResolver,
                            Settings.Secure.LOCATION_MODE
                        )

                        if (locationMode != Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                            secureSettingsMethod.invoke(
                                null,
                                contentResolver,
                                Settings.Secure.LOCATION_MODE,
                                Settings.Secure.LOCATION_MODE_HIGH_ACCURACY
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Handle any exceptions that may occur during the process
                    }

                    // Method 2: Enable GPS by sending an Intent to the GPS settings activity
                    try {
                        val gpsIntent = Intent("android.location.GPS_ENABLED_CHANGE")
                        gpsIntent.putExtra("enabled", true)
                        sendBroadcast(gpsIntent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Handle any exceptions that may occur during the process
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        } else {
            // GPS is already enabled
            // Start your GPS-related operations here
        }
    }

    fun checkGpsOnOrNot() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()
        val locationSettingsRequestBuilder = LocationSettingsRequest.Builder()
        locationSettingsRequestBuilder.addLocationRequest(locationRequest)
        locationSettingsRequestBuilder.setAlwaysShow(true)
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build())
        task.addOnSuccessListener(
            this
        ) {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        task.addOnFailureListener(this) { e ->



            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        this@LocationEnableActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendIntentException: SendIntentException) {
                    sendIntentException.printStackTrace()
                }
            }
        }
    }

}