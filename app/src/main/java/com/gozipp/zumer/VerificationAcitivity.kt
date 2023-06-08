package com.gozipp.zumer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.gozipp.zumer.Onboarding.Onboarding
import com.gozipp.zumer.databinding.ActivityVerificationAcitivityBinding
import com.gozipp.zumer.utills.KeyboardUtils

class VerificationAcitivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationAcitivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOtpFocus()
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.llView.setOnClickListener {
            KeyboardUtils.hideKeyboard(this, it)
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, Onboarding::class.java))
        }
    }

    private fun setOtpFocus() {
        val editTextList = listOf(
            binding.editText1,
            binding.editText2,
            binding.editText3,
            binding.editText4,
            binding.editText5,
            binding.editText6
        )

        for (i in editTextList.indices) {
            editTextList[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {


                    if (s?.length == 1) {
                        val nextIndex = (i + 1) % editTextList.size
                        editTextList[nextIndex].requestFocus()
                    } else if (s?.length == 0) {
                        val prevIndex = if (i == 0) editTextList.size - 1 else i - 1
                        editTextList[prevIndex].requestFocus()
                    }

                    val allTextFilled = editTextList.all { it.text.length == 1 }
                    binding.btnLogin.isSelected = allTextFilled


                }
            })
        }

    }
}