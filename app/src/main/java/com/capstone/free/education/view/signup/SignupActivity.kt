package com.capstone.free.education.view.signup

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.free.education.data.remote.response.RegisterRequest
import com.capstone.free.education.data.remote.response.RegisterResponse
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import com.capstone.free.education.databinding.ActivitySignupBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditText.error = "Email tidak valid"
                Log.e("Signup Validation", "Email tidak valid: $email")
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                return@setOnClickListener
            }

            Log.d("Signup Input", "Email: $email, Username: $username, Password: $password")

            val registerRequest = RegisterRequest(email = email, username = username, password = password)

            lifecycleScope.launch {
                try {
                    val response = ApiConfig.getApiService().register(registerRequest)
                    if (response.isSuccessful) {
                        val body = response.body()
                        Log.d("Signup Success", "Response: $body")
                        Toast.makeText(this@SignupActivity, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("Signup Error", "Error Body: $errorBody")
                        Toast.makeText(this@SignupActivity, "Registrasi gagal: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("Signup Exception", "Error: ${e.message}")
                    Toast.makeText(this@SignupActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}




