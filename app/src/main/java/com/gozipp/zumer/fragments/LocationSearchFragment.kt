package com.gozipp.zumer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.gozipp.views.LocationViewModel
import com.gozipp.zumer.R
import com.gozipp.zumer.databinding.FragmentHomeBinding
import com.gozipp.zumer.databinding.FragmentLocationSearchBinding


class LocationSearchFragment : Fragment() {

    private var _binding : FragmentLocationSearchBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocationSearchBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        Places.initialize(requireContext(), "AIzaSyDGTNMJhlUjoSOggpK3Gsvkm5aRUXtOUCg")
       val placesClient = Places.createClient(requireContext())

        // Perform autocomplete API call
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("IN") // Set your desired country
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery("Search Place from destination")
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            for (prediction in response.autocompletePredictions) {
                val description = prediction.getPrimaryText(null).toString()

                // Set the text of the EditText
                binding.etSearchLocation.setText(description)
            }
        }.addOnFailureListener { exception ->
            // Handle error
        }



        val locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        binding.btnSearchLocation.setOnClickListener {
            val address = binding.etSearchLocation.text.toString()
            if (address != "") {
                locationViewModel.addLocation(address)
                activity?.onBackPressed()
            } else {
                binding.etSearchLocation.error = "Please enter area"
            }
        }
    }

}