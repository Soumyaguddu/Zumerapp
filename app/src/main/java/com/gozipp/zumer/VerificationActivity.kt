package com.gozipp.zumer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.gozipp.zumer.databinding.ActivityVerificationAcitivityBinding
import com.gozipp.zumer.utills.Constant
import com.gozipp.zumer.utills.Constant.LOCATION_CHECK
import com.gozipp.zumer.utills.Constant.LOGIN_CHECK
import com.gozipp.zumer.utills.KeyboardUtils
import com.gozipp.zumer.utills.PreferenceHelper

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationAcitivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null && intent.extras != null) {
           val  mobileNumber = intent.getStringExtra("mobileNumber").toString()
            binding.tvMobileNumberView.text="We have sent an OTP to this $mobileNumber"
        }

        setOtpFocus()

        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.llView.setOnClickListener {
            KeyboardUtils.hideKeyboard(this, it)
        }
        binding.btnLogin.setOnClickListener {



            PreferenceHelper.writeBooleanToPreference(LOGIN_CHECK, false)

            if (PreferenceHelper.getBooleanFromPreference(Constant.ADD_USER))
            {
                startActivity(Intent(this, AddUserActivity::class.java))

            }
            else
            {
                if (PreferenceHelper.getBooleanFromPreference(LOCATION_CHECK)) {
                    val intent = Intent(this, LocationEnableActivity::class.java)
                    startActivity(intent)
                } else {

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }

            }
            finish()


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
                   hideKeyboard()
                    binding.btnLogin.isSelected = allTextFilled


                }
            })
        }

    }
    private  fun  hideKeyboard(){
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.btnLogin, InputMethodManager.SHOW_IMPLICIT)
    }

}