package com.capstone.free.education.view.splash

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.free.education.R
import com.capstone.free.education.view.ViewModelFactory
import com.capstone.free.education.view.main.MainActivity
import com.capstone.free.education.view.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Layout untuk Splash Screen

        // Inisialisasi logo
        val logo = findViewById<ImageView>(R.id.splash_logo)

        // Animasi fade-in untuk logo
        ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f).apply {
            duration = 2000 // 2 detik
        }.start()

        // Inisialisasi ViewModel
        val factory = ViewModelFactory.getInstance(this)
        val viewModel: SplashViewModel by viewModels { factory }

        // Delay 10 detik sebelum memeriksa token
        lifecycleScope.launch {
            delay(10000) // 10 detik
            viewModel.getUserToken().collect { token ->
                if (token.isNullOrEmpty()) {
                    // Jika token kosong atau null, arahkan ke WelcomeActivity
                    val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Jika token tersedia, arahkan ke MainActivity
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
