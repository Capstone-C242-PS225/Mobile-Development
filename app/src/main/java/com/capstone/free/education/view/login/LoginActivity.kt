package com.capstone.free.education.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.capstone.free.education.data.pref.UserModel
import com.capstone.free.education.data.pref.dataStore
import com.capstone.free.education.databinding.ActivityLoginBinding
import com.capstone.free.education.view.ViewModelFactory
import com.capstone.free.education.view.main.MainActivity
import com.capstone.free.education.view.setting.SettingPreferences
import com.capstone.free.education.view.setting.SettingViewModel


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, SettingPreferences.getInstance(this.dataStore))
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this, SettingPreferences.getInstance(applicationContext.dataStore)))
            .get(SettingViewModel::class.java)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString() // Tambahkan pengambilan password

            // Validasi password
            if (password.length < 8) {
                binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                return@setOnClickListener // Hentikan eksekusi jika password tidak valid
            }

            // Jika validasi lolos, lanjutkan proses login
            viewModel.saveSession(UserModel(email, "sample_token"))
            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }

        // Tambahkan TextWatcher untuk menampilkan error saat password diubah
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak diperlukan
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                } else {
                    binding.passwordEditText.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak diperlukan
            }
        })
    }


}