package com.gozipp.zumer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.gozipp.zumer.databinding.ActivityMainBinding
import com.gozipp.zumer.utills.Constant
import com.gozipp.zumer.utills.VolleyErrorParser
import com.gozipp.zumer.utills.VolleySingleton
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSave.setOnClickListener {
            if (binding.etFirstName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide your First Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.etFirstName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide Last Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.etEmail.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide your Email Id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.etPhoneNo.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide your Phone No.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            addUser()

        }
    }

    private fun addUser() {
        val url = Constant.BASE_URL + "auth/AddUser"

        val jsonObject = JSONObject()
        jsonObject.put("firstName", binding.etFirstName.text.toString().trim())
        jsonObject.put("lastName", binding.etLastName.text.toString().trim())
        jsonObject.put("phoneNum", binding.etPhoneNo.text.toString().trim())
        jsonObject.put("email", binding.etEmail.text.toString().trim())
        jsonObject.put("address", binding.etAddress.text.toString().trim())
        val productArray = JSONArray()
        jsonObject.put("saveProduct", productArray)
        val likedProduct = JSONArray()
        jsonObject.put("saveProduct", likedProduct)
        jsonObject.put("type", "")

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                if (!response.isNullOrEmpty()) {
                    val jsonObject=JSONObject(response)
                    if (jsonObject.getString("output")=="1")
                    {
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    }

                }
            },
            { error ->
                val error = error?.let {
                    VolleyErrorParser.getVolleyErrorMessage(it)
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = java.util.HashMap<String, String>()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                return jsonObject.toString().toByteArray()
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

}