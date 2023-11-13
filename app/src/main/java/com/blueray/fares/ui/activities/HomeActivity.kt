package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityHomeBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show

class HomeActivity : BaseActivity() {
    private lateinit var navController: NavController

    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNav, navController)
//        NavigationUI.setupWithNavController(binding.navDrawer, navController)

binding.addNew.setOnClickListener{
    startActivity(Intent(this,UploadeVedio::class.java))

}
        binding.bottomNav.setOnItemSelectedListener {
                item ->
            when(item.itemId){
                R.id.home ->{
//                    val v = Bundle()
//                    v.putString("123","1")
                    navController.navigate(R.id.homeVidFrag)
                    true
                }
                R.id.PlaceHolder->{
                    startActivity(Intent(this,UploadeVedio::class.java))
                    true
                }
                else ->{
                    false
                }
            }
        }

    }


    fun showBottomNav(){
        binding.bottomNav.show()
    }
    fun hideBottomNav(){
        binding.bottomNav.hide()
    }
}