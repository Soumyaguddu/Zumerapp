package com.gozipp.zumer.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gozipp.zumer.R
import com.gozipp.zumer.databinding.FragmentBottomSheetBinding
import com.gozipp.zumer.databinding.FragmentHomeBinding
import com.gozipp.zumer.viewModel.AfterClickingRideNow
import com.gozipp.zumer.viewModel.DistanceViewModel


class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var pendingIntent: PendingIntent
    private var _binding : FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  val paymentMethodFragment = PaymentMethodFragment()
       binding. bottomRelative3.setOnClickListener {
            paymentMethodFragment.show(parentFragmentManager, paymentMethodFragment.tag)
        }*/
    }

    override fun onResume() {
        super.onResume()
        val distanceViewModel = ViewModelProviders.of(this).get(DistanceViewModel::class.java)
        val afterClickingRideNow = ViewModelProviders.of(this).get(AfterClickingRideNow::class.java)


        distanceViewModel.getDistance().observe(viewLifecycleOwner, Observer {
            val distance = it.toString()
            setAmount(it)
            binding.tvTotalDistance.text = distance + " KM"
        })

      /*  binding.btnRequestRide.setOnClickListener {
//            startActivity(Intent(context, RiderComing::class.java))


            val intent = Intent(context, RiderComing::class.java)

            pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            dislayNotification("Ride Booked", "The Rider is on his way to your location")
            afterClickingRideNow.setMapRider(1)
            dismiss()
        }*/
    }

    private fun setAmount(distance: Float) {
        val totalAmount: Int = (distance * 10).toInt()
        binding.tvTotalRupee.text = totalAmount.toString()
    }

    private fun dislayNotification(task: String, desc: String) {
        val notificationManager =
            requireActivity().applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "workExample",
                "workExample",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val builder =
            NotificationCompat.Builder(requireActivity().applicationContext, "workExample")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.motorbike)
                .setContentIntent(pendingIntent)
        notificationManager.notify(1, builder.build())
    }

}
