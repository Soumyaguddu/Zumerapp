package com.gozipp.zumer

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.gozipp.zumer.databinding.ActivityVerificationAcitivityBinding

class VerificationAcitivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationAcitivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}