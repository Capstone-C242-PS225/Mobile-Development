package com.capstone.free.education.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.free.education.R
import com.capstone.free.education.data.pref.dataStore
import com.capstone.free.education.databinding.ActivityMainBinding
import com.capstone.free.education.view.ViewModelFactory
import com.capstone.free.education.view.home.HomeFragment
import com.capstone.free.education.view.konsultasi.KonsultasiFragment
import com.capstone.free.education.view.login.LoginActivity
import com.capstone.free.education.view.profile.ProfileFragment
import com.capstone.free.education.view.setting.SettingFragment
import com.capstone.free.education.view.setting.SettingPreferences
import com.capstone.free.education.view.setting.SettingViewModel
import com.capstone.free.education.view.spendingscore.spendingscoreFragment
import com.capstone.free.education.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext, SettingPreferences.getInstance(applicationContext.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this, SettingPreferences.getInstance(applicationContext.dataStore))
        ).get(SettingViewModel::class.java)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            val desiredMode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            if (currentMode != desiredMode) {
                AppCompatDelegate.setDefaultNightMode(desiredMode)
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_spending -> {
                    loadFragment(spendingscoreFragment())
                    true
                }
                R.id.nav_setting -> {
                    loadFragment(SettingFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        viewModel.getSession().observe(this) { user ->
            val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

            if (!isLoggedIn) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        setupView()
    }

    private fun loadFragment(fragment: Fragment) {
        Log.d("FragmentLoad", "Loading fragment: ${fragment.javaClass.simpleName}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
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
}
