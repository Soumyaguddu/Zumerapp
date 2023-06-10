package com.gozipp.zumer

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.gozipp.zumer.databinding.ActivityAddUserBinding
import com.gozipp.zumer.utills.Constant
import com.gozipp.zumer.utills.Constant.LOCATION_CHECK
import com.gozipp.zumer.utills.PreferenceHelper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class AddUserActivity : BaseActivity() {
    private lateinit var binding: ActivityAddUserBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnAddUser.setOnClickListener {

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            updatePreference(name, phone)


            PreferenceHelper.writeBooleanToPreference(Constant.ADD_USER, true)
            if (PreferenceHelper.getBooleanFromPreference(LOCATION_CHECK)) {
                val intent = Intent(this, LocationEnableActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }




            finish()

        }
    }

    private fun updatePreference(name: String, phone: String) {
        PreferenceHelper.writeStringToPreference(Constant.KEY_DISPLAY_NAME, name)
        PreferenceHelper.writeStringToPreference(Constant.USER_PHONE_LOGIN, phone)
    }

    private fun setButtonEnabled(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val regex = Regex("^[+]?(?:[0-9]\\s*){10,13}\$")
        if (binding.etName.text.toString().isEmpty()) {
            binding.btnAddUser.isSelected = false
            return false
        } else if (binding.etEmail.text.toString().isEmpty()) {
            binding.btnAddUser.isSelected = false
            return false
        } else if (!binding.etEmail.text.toString().matches(emailPattern.toRegex())) {
            binding.btnAddUser.isSelected = false
            return false
        } else if (binding.etPhone.text.toString().isEmpty()) {
            binding.btnAddUser.isSelected = false
            return false
        } else if (binding.etPhone.text.toString().length < 10) {
            binding.btnAddUser.isSelected = false
            return false
        } else if (!binding.etPhone.text.toString().matches(regex)) {
            binding.btnAddUser.isSelected = false
            return false
        }
        binding.btnAddUser.isSelected = true
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {


        binding.etName.afterTextChanged {
            if (binding.etName.text.toString().isEmpty()) {
                binding.editName.isErrorEnabled = true
                binding.editName.error = "Put name"
                binding.etName.requestFocus()
                binding.btnAddUser.isSelected = false
                return@afterTextChanged

            }
            binding.profileName.text = binding.etName.text.toString().trim()
            setButtonEnabled()
            binding.editName.error = null
            binding.editName.isErrorEnabled = false

        }
        binding.etEmail.afterTextChanged {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (binding.etEmail.text.toString().isEmpty()) {
                binding.editEmail.isErrorEnabled = true
                binding.editEmail.error = "Put email Id"
                binding.etEmail.requestFocus()
                binding.btnAddUser.isSelected = false
                return@afterTextChanged
            } else if (!binding.etEmail.text.toString().matches(emailPattern.toRegex())) {
                binding.editEmail.isErrorEnabled = true
                binding.editEmail.error = "Valid email id required"
                binding.etEmail.requestFocus()
                binding.btnAddUser.isSelected = false
                return@afterTextChanged
            }
            binding.profileEmail.text = binding.etEmail.text.toString().trim()
            setButtonEnabled()
            binding.editEmail.error = null
            binding.editEmail.isErrorEnabled = false

        }
        binding.etPhone.afterTextChanged {
            val regex = Regex("^[+]?(?:[0-9]\\s*){10,13}\$")
            if (binding.etPhone.text.toString().isEmpty()) {
                binding.editPhone.isErrorEnabled = true
                binding.editPhone.error = "Mobile number required"
                binding.btnAddUser.isSelected = false
                binding.etPhone.requestFocus()
                return@afterTextChanged
            } else if (binding.etPhone.text.toString().length < 10) {
                binding.editPhone.isErrorEnabled = true
                binding.editPhone.error = "Mobile number must be 10 digit"
                binding.btnAddUser.isSelected = false
                binding.etPhone.requestFocus()
                return@afterTextChanged
            } else if (!binding.etPhone.text.toString().matches(regex)) {
                binding.editPhone.isErrorEnabled = true
                binding.editPhone.error = "Valid mobile number required"
                binding.btnAddUser.isSelected = false
                binding.etPhone.requestFocus()
                return@afterTextChanged
            }
            hideKeyboard()
            binding.profilePhone.text = binding.etPhone.text.toString().trim()
            setButtonEnabled()
            binding.editPhone.isErrorEnabled = false
            binding.editPhone.error = null
        }
        binding.etDateOfBirth.setOnClickListener {


            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    binding.etDateOfBirth.setText(formattedDate)
                    val age = getDateComponents(formattedDate)
                    if (age != null) {
                        val (years, months, days) = age
                        val ageNew = "Age is: $years Years $months Months $days Days"
                        binding.dateYears.text = ageNew
                        println("Years: $years, Months: $months, Days: $days")
                    } else {
                        println("Invalid date format")
                    }
                },
                currentYear,
                currentMonth,
                currentDay
            )

            datePickerDialog.show()

        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateComponents(dateString: String): Triple<Int, Int, Int>? {
        try {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val birthDate = LocalDate.parse(dateString, dateFormatter)
            val currentDate = LocalDate.now()

            val period = Period.between(birthDate, currentDate)

            val years = period.years
            val months = period.months
            val days = period.days

            return Triple(years, months, days)
        } catch (e: Exception) {
            return null
        }


    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.btnAddUser, InputMethodManager.SHOW_IMPLICIT)
    }
}
