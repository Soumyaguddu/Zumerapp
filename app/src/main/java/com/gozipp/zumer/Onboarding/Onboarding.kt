package com.gozipp.zumer.Onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gozipp.zumer.databinding.ActivityLoginBinding
import com.gozipp.zumer.databinding.ActivityOnboardingBinding

class Onboarding : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}