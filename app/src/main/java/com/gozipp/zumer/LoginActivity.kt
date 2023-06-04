package com.gozipp.zumer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.gozipp.zumer.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etMobileNo.afterTextChanged { s ->

            binding.btnLogin.isSelected = s.length == 10

        }




        binding.btnLogin.setOnClickListener {
            if (binding.etMobileNo.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide your mobile no", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.etMobileNo.text.toString().length < 10) {
                Toast.makeText(this, "Please provide valid mobile no", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startActivity(Intent(this, VerificationAcitivity::class.java))
        }


    }
}