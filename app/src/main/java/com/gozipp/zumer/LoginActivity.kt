package com.gozipp.zumer


import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsApi
import com.google.android.gms.auth.api.credentials.HintRequest
import com.gozipp.zumer.Onboarding.Onboarding
import com.gozipp.zumer.databinding.ActivityLoginBinding
import com.gozipp.zumer.utills.KeyboardUtils

class LoginActivity : BaseActivity() {
    companion object {
        private val PERMISSIONS_REQUEST_READ_PHONE_STATE = 1
    }


    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etMobileNo.afterTextChanged { s ->

            if (binding.etMobileNo.text.toString().length < 10) {
                return@afterTextChanged
            } else if (!isValidMobileNumber(binding.etMobileNo.text.toString())) {
                return@afterTextChanged
            }
            binding.btnLogin.isSelected = true

        }
        binding.etMobileNo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.etMobileNo.text.toString().length < 10) {
                    Toast.makeText(
                        this,
                        "Please provide 10-digit mobile number",
                        Toast.LENGTH_SHORT
                    ).show()
                    @Suppress("LABEL_NAME_CLASH")
                    return@setOnEditorActionListener false
                } else if (!isValidMobileNumber(binding.etMobileNo.text.toString())) {
                    Toast.makeText(this, "Please provide a valid mobile number", Toast.LENGTH_SHORT)
                        .show()
                    @Suppress("LABEL_NAME_CLASH")
                    return@setOnEditorActionListener false
                }
                checkLogin()
                true
            } else {
                false
            }
        }

        binding.llView.setOnClickListener {
            KeyboardUtils.hideKeyboard(this, it)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etMobileNo.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide your mobile no", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.etMobileNo.text.toString().length < 10) {
                Toast.makeText(this, "Please provide 10 digit mobile no", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!isValidMobileNumber(binding.etMobileNo.text.toString())) {
                Toast.makeText(this, "Please provide valid mobile no", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            checkLogin()
        }
        requestHint()
    }




    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()


        val credentialsClient = Credentials.getClient(this)
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(
                intent.intentSender,
                PERMISSIONS_REQUEST_READ_PHONE_STATE,
                null, 0, 0, 0
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && resultCode == RESULT_OK) {

            // get data from the dialog which is of type Credential
            val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)

            // set the received data t the text view
            credential?.apply {
                Log.e("creddd",credential.id)

                val digitsOnly = credential.id.filter { it.isDigit() }
                val transformedNumber = digitsOnly.substring(2)
                // Store the digits in a variable
                binding.etMobileNo.setText(transformedNumber)
                binding.etMobileNo.setSelection( binding.etMobileNo.text!!.length)
                checkLogin()
            }
        } else if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            Toast.makeText(this, "No phone numbers found", Toast.LENGTH_LONG).show()
            binding.etMobileNo.requestFocus()
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etMobileNo, InputMethodManager.SHOW_IMPLICIT)
        } else {
            binding.etMobileNo.requestFocus()
        }
    }
    private fun checkLogin() {
        binding.btnLogin.isSelected = true
        startActivity(Intent(this, VerificationAcitivity::class.java))
    }

    fun isValidMobileNumber(number: String): Boolean {
        val regex = Regex("^[+]?(?:[0-9]\\s*){10,13}\$")
        return regex.matches(number.trim())
    }
}