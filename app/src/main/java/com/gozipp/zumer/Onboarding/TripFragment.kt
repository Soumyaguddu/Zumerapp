package com.gozipp.zumer.Onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gozipp.zumer.*
import com.gozipp.zumer.databinding.FragmentRideBinding
import com.gozipp.zumer.databinding.FragmentTripBinding
import com.gozipp.zumer.utills.Constant.LOGIN_CHECK
import com.gozipp.zumer.utills.PreferenceHelper


class TripFragment : Fragment() {
    private var buttonClickListener: OnButtonClickListener? = null
    private lateinit var binding : FragmentTripBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClickListener) {
            buttonClickListener = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        buttonClickListener = null
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

            PreferenceHelper.writeBooleanToPreference(LOGIN_CHECK, false)
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            buttonClickListener?.onButtonClick()

        }
    }

}