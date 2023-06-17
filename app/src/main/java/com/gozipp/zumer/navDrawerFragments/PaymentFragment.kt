package com.gozipp.zumer.navDrawerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.gozipp.zumer.R


class PaymentFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.my_toolbar)

        // Set the title for the Toolbar
        toolbar?.title = "Payment"
        // Inflate the layout for this fragment
        return view
    }


}