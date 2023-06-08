package com.gozipp.zumer.Onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter (private val lifecycle: Lifecycle, fm: FragmentManager, internal var totalTabs: Int): FragmentStateAdapter(fm,lifecycle){

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0-> DestinationFragment()
            1-> RideFragment()
            2-> TripFragment()

            else -> DestinationFragment()
        }
    }
}