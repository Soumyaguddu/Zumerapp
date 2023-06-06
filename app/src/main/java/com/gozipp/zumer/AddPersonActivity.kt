package com.gozipp.zumer

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.gozipp.zumer.databinding.ActivityAddPersonBinding
import java.util.*

class AddPersonActivity : BaseActivity() {
    private lateinit var binding: ActivityAddPersonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        binding.imgBack.setOnClickListener {
            finish()
        }

    }

    private fun setData() {


        binding.etName.afterTextChanged {

            binding.profileName.text = binding.etName.text.toString().trim()
        }
        binding.etEmail.afterTextChanged {

            binding.profileEmail.text = binding.etEmail.text.toString().trim()

        }
        binding.etPhone.afterTextChanged {

            binding.profilePhone.text = binding.etPhone.text.toString().trim()
        }
        binding.etDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this@AddPersonActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    // Update the TextInputEditText with the selected date
                    val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                    binding.etDateOfBirth.setText(selectedDate)

                }, year, month, day
            )

            datePickerDialog.show()
        }


    }
}