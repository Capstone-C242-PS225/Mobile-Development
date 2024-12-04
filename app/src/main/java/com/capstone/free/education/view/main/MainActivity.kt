package com.capstone.free.education.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.capstone.free.education.R
import com.capstone.free.education.databinding.ActivityMainBinding
import com.capstone.free.education.view.ViewModelFactory
import com.capstone.free.education.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        if (navHostFragment is NavHostFragment) {
            val navController = navHostFragment.navController

            // Menyambungkan BottomNavigationView dengan NavController
            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
            NavigationUI.setupWithNavController(bottomNavigationView, navController)

            // Handle pergerakan fragment berdasarkan pilihan bottom navigation
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                        navController.navigate(R.id.home_nav)
                        true
                    }
                    R.id.nav_setting -> {
                        navController.navigate(R.id.setting_nav)
                        true
                    }
                    R.id.nav_profile -> {
                        navController.navigate(R.id.profile_nav)
                        true
                    }
                    else -> false
                }
            }
        } else {
            Log.e("MainActivity", "NavHostFragment tidak ditemukan atau cast gagal")
        }



        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

}