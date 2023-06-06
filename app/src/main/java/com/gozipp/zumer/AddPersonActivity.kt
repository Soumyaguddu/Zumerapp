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

    }

    private fun setData() {
        val editTextList = listOf(
            binding.etName,
            binding.etEmail,
            binding.etPhone,
            binding.etDateOfBirth
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

                    val allTextFilled = editTextList.all { it.text?.length == 1 }
                    binding.btnLogin.isSelected = allTextFilled


                }
            })
        }

        binding.etName.afterTextChanged {

            if (binding.etName.text.toString().isNotEmpty()) {
                binding.profileName.text = binding.etName.text.toString().trim()
            } else {
                binding.profileName.text = ""
            }
        }
        binding.etEmail.afterTextChanged {

            if (binding.etEmail.text.toString().isNotEmpty()) {
                binding.profileEmail.text = binding.etEmail.text.toString().trim()
            } else {
                binding.profileEmail.text = ""
            }

        }
        binding.etPhone.afterTextChanged {

            if (binding.etPhone.text.toString().isNotEmpty()) {
                binding.profilePhone.text = binding.etPhone.text.toString().trim()
            } else {
                binding.profilePhone.text = ""
            }

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