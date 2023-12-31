package com.gozipp.zumer.navDrawerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.gozipp.zumer.R
import com.gozipp.zumer.databinding.FragmentHomeBinding
import com.gozipp.zumer.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.my_toolbar)

        // Set the title for the Toolbar
        toolbar?.title = "Settings"
        return binding.root
    }


}