package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Validasi password minimal 8 karakter
            if (password.length < 8) {
                binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                return@setOnClickListener
            }

            // Jika validasi lolos, tampilkan dialog sukses
            AlertDialog.Builder(this).apply {
                setTitle("Registrasi Berhasil")
                setMessage("Akun dengan email $email berhasil dibuat. Yuk, login!")
                setPositiveButton("OK") { _, _ ->
                    finish()
                }
                create()
                show()
            }
        }

        // Tambahkan TextWatcher untuk validasi real-time
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 8) {
                    binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                } else {
                    binding.passwordEditText.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}