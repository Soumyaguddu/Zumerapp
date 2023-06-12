package com.gozipp.zumer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.gozipp.zumer.databinding.ActivityHomeBinding
import com.gozipp.zumer.navDrawerFragments.`interface`.CleanActivityFromFragmentInterface
import com.gozipp.zumer.utills.Constant
import com.gozipp.zumer.utills.PreferenceHelper
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener ,CleanActivityFromFragmentInterface{
    private lateinit var binding: ActivityHomeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.myToolbar)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.itemIconTintList = null
        val hView = binding.navView.getHeaderView(0)

        val nav_user = hView.findViewById<TextView>(R.id.tv_user_name)



        val nav_profile = hView.findViewById<CircleImageView>(R.id.ivProfile)

        binding.navView.setNavigationItemSelectedListener(this)

        val name = PreferenceHelper.getStringFromPreference(Constant.KEY_DISPLAY_NAME)
        val phone = PreferenceHelper.getStringFromPreference(Constant.USER_PHONE_LOGIN)
        if (name != null&&phone!=null) {
            nav_user.text = name.trim()+"\n"+phone.trim()
        }

binding.menu.setOnClickListener {


    binding.drawerLayout.openDrawer(GravityCompat.START)
}

    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(
                this,
                R.id.fragmentContainerView
            ), binding.drawerLayout
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> return if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            } else false
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        startActivity(Intent(this,LocationEnableActivity::class.java))
        finish()
    }
}