package com.gozipp.zumer.Onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gozipp.zumer.AddPersonActivity
import com.gozipp.zumer.R
import com.gozipp.zumer.databinding.FragmentRideBinding
import com.gozipp.zumer.databinding.FragmentTripBinding


class TripFragment : Fragment() {
    private lateinit var binding : FragmentTripBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTripBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStarted.setOnClickListener {
            startActivity(Intent(context, AddPersonActivity::class.java))
        }
    }

}