package com.gozipp.zumer


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        requestPhoneStatePermission()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun requestPhoneStatePermission() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_NUMBERS
        )

        val permissionDeniedList = mutableListOf<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionDeniedList.add(permission)
            }
        }

        if (permissionDeniedList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionDeniedList.toTypedArray(),
                PERMISSIONS_REQUEST_READ_PHONE_STATE
            )
        } else {
            // All permissions are already granted, proceed with retrieving the phone number
            performGoogleSignIn()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions are granted, proceed with retrieving the phone number
                performGoogleSignIn()
            } else {
                // Permissions are denied, handle accordingly
                // You can show an error message or provide an alternative flow
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun performGoogleSignIn() {
        // Initiate Google Sign-In process using GoogleSignInOptions and GoogleSignInClient

        // Once the Google Sign-In is successful, retrieve the phone number
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val subscriptionManager =
                getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            val simInfoList = subscriptionManager.activeSubscriptionInfoList
            val simNumbers = simInfoList?.map { it.number ?: "" }


            val inflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.sim_dialog_item, null)
            val tvSimNumbersOne = dialogView.findViewById<TextView>(R.id.tvSimNumbersOne)
            val tvSimNumbersTwo = dialogView.findViewById<TextView>(R.id.tvSimNumbersTwo)
            val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
            try {
                tvSimNumbersOne.text = simInfoList[0].number
            } catch (e: Exception) {
            }
            try {
                tvSimNumbersTwo.text = simInfoList[1].number
            } catch (e: Exception) {
            }


            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)

            val dialog = builder.create()
            tvSimNumbersOne.setOnClickListener {
                binding.etMobileNo.setText(tvSimNumbersOne.text.toString())
                binding.etMobileNo.setSelection(binding.etMobileNo.length())
                dialog.dismiss()
            }
            tvSimNumbersTwo.setOnClickListener {
                binding.etMobileNo.setText(tvSimNumbersTwo.text.toString())
                binding.etMobileNo.setSelection(binding.etMobileNo.length())
                dialog.dismiss()
            }
            btnOk.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()


        } else {
            // Permission is not granted, handle accordingly
            // You can show an error message or provide an alternative flow
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