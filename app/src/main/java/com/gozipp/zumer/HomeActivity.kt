package com.gozipp.zumer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.navigation.NavigationView
import com.gozipp.zumer.databinding.ActivityHomeBinding
import com.gozipp.zumer.navDrawerFragments.`interface`.CleanActivityFromFragmentInterface
import com.gozipp.zumer.utills.Constant
import com.gozipp.zumer.utills.PreferenceHelper
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener ,CleanActivityFromFragmentInterface{
    private lateinit var binding: ActivityHomeBinding
    private val REQUEST_CHECK_SETTINGS = 0x1
    private var locationManager: LocationManager? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.itemIconTintList = null
        val hView = binding.navView.getHeaderView(0)

        val nav_user = hView.findViewById<TextView>(R.id.tv_user_name)
        binding.navView.itemIconTintList = null
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        val nav_profile = hView.findViewById<CircleImageView>(R.id.ivProfile)

        binding.navView.setNavigationItemSelectedListener(this)

        val name = PreferenceHelper.getStringFromPreference(Constant.KEY_DISPLAY_NAME)
        val phone = PreferenceHelper.getStringFromPreference(Constant.USER_PHONE_LOGIN)
        if (name != null&&phone!=null) {
            nav_user.text = name.trim()+"\n"+phone.trim()
        }

        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        }
        else {
            checkGpsOnOrNot()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(
                this,
                R.id.fragmentContainerView
            ), binding.drawerLayout
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> return if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            } else false
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onRefresh() {
        startActivity(Intent(this,LocationEnableActivity::class.java))
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home -> {
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                Navigation.findNavController(this, R.id.fragmentContainerView)
                    .navigate(R.id.homeFragment, null, navOptions)
            }

            R.id.nav_settings -> {
                if (isValidDestination(R.id.settingsFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.settingsFragment)
                }
            }
            R.id.nav_payment -> {
                if (isValidDestination(R.id.paymentFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.paymentFragment)
                }
            }
            R.id.nav_myRides -> {
                if (isValidDestination(R.id.myRidesFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.myRidesFragment)
                }
            }

        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun isValidDestination(destination: Int): Boolean {
        return destination != Navigation.findNavController(this, R.id.fragmentContainerView)
            .currentDestination?.id
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

        }
        task.addOnFailureListener(this) { e ->



            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        this@HomeActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendIntentException: IntentSender.SendIntentException) {
                    sendIntentException.printStackTrace()
                }
            }
        }
    }

}