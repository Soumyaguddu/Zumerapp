package com.gozipp.zumer.utills
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.widget.Toast

class MyLocationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == LocationManager.KEY_LOCATION_CHANGED) {
            val location: Location? =
                intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED)
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                // Do something with the received location data
                showToast(context, "Latitude: $latitude, Longitude: $longitude")
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
