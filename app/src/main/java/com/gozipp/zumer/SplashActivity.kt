package com.gozipp.zumer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gozipp.zumer.Onboarding.Onboarding
import com.gozipp.zumer.utills.PreferenceHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        PreferenceHelper.getSharedPreferences(this)
        Handler().postDelayed({
            if (PreferenceHelper.getBooleanFromPreference("loginCheck")) {
                val intent = Intent(this, Onboarding::class.java)
                startActivity(intent)
            } else {

                if (PreferenceHelper.getBooleanFromPreference("locationCheck")) {
                    val intent = Intent(this, LocationEnableActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }


            }
        },2000)




    }
}