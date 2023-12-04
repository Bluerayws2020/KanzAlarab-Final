package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.window.SplashScreen
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityHomeBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.HelperUtils.setDefaultLanguage
import com.blueray.fares.helpers.HelperUtils.setLang
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.videoliveeventsample.view.fragment.LiveEventListFragment

class HomeActivity : BaseActivity() {
    private lateinit var navController: NavController
    private val liveEventListFragment = LiveEventListFragment()

    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        setDefaultLanguage(this@HomeActivity,"ar")
        setLang(this@HomeActivity,"ar")










        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNav, navController)
//        NavigationUI.setupWithNavController(binding.navDrawer, navController)
        binding.addNew.setOnClickListener{

            if (HelperUtils.getUid(this@HomeActivity) == "0"){
                Toast.makeText(this,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                startActivity(Intent(this,com.blueray.fares.ui.activities.SplashScreen::class.java))
                finish()

            }else {
                MyBottomSheetFragment().show(supportFragmentManager, MyBottomSheetFragment::class.java.simpleName)




            }
        }


        binding.bottomNav.setOnItemSelectedListener {
                item ->
            when(item.itemId){
                R.id.home ->{
//                    val v = Bundle()
//                    v.putString("123","1")
                    navController.navigate(R.id.homePagerFragment)
                    true
                }
                R.id.search->{
//                    replace(R.id.fragmentContainerView, liveEventListFragment)

navController.navigate(R.id.liveEventListFragment)
                   true
                }
                R.id.PlaceHolder->{

                    if (HelperUtils.getUid(this@HomeActivity) == "0"){
                        Toast.makeText(this,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                        startActivity(Intent(this,MainActivity::class.java))
                        finish()

                        true
                    }else {

                        startActivity(Intent(this,UploadeVedio::class.java))
                        true

                    }

                    true
                }
                else ->{
                    if (HelperUtils.getUid(this@HomeActivity) == "0"){
                        Toast.makeText(this,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                        startActivity(Intent(this,MainActivity::class.java))
                        finish()

                        false
                    }else {
                        navController.navigate(R.id.notificationFragment)
true
                    }
                    true
                }
            }
        }

    }

//    private fun setUpDrawerNavigation() {
//        binding.navDrawer.setNavigationItemSelectedListener {
//                menuItem->
//            when(menuItem.itemId){
//                R.id.home->{
//                    navController.navigate(R.id.home)
//                    closeDrawer()
//                    true
//                }
////                R.id.
//
//            }
//
//
//            }
//    }


    fun showBottomNav(){
        binding.bottomNav.show()
    }
    fun hideBottomNav(){
        binding.bottomNav.hide()
    }

//        fun openDrawer(){
//            binding.drawerLayout.openDrawer(GravityCompat.START)
//        }
//        fun closeDrawer(){
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//        }



    }