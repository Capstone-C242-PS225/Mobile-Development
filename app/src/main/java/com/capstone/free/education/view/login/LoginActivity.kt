package com.capstone.free.education.view.login

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstone.free.education.R
import com.capstone.free.education.data.pref.UserModel
import com.capstone.free.education.data.pref.dataStore
import com.capstone.free.education.databinding.ActivityLoginBinding
import com.capstone.free.education.view.ViewModelFactory
import com.capstone.free.education.view.main.MainActivity
import com.capstone.free.education.view.setting.SettingPreferences
import com.capstone.free.education.view.setting.SettingViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, SettingPreferences.getInstance(this.dataStore))
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup dark mode
        val settingViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this, SettingPreferences.getInstance(applicationContext.dataStore)))
            .get(SettingViewModel::class.java)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        setupView()
        setupAction()
        auth = Firebase.auth
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
            val password = binding.passwordEditText.text.toString()

            // Validasi password
            if (password.length < 8) {
                binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                return@setOnClickListener
            }

            // Simpan sesi manual ke SharedPreferences
            val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("isLoggedIn", true)
                apply()
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
                    finish() // Pastikan LoginActivity selesai
                }
                create()
                show()
            }
        }


        // Tambahkan TextWatcher untuk menampilkan error saat password diubah
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                } else {
                    binding.passwordEditText.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Tombol Google Sign-In
        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.your_web_client_id)) // Gunakan client ID dari Firebase
            .build()
        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            // Simpan status login ke SharedPreferences
            val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("isLoggedIn", true)
                apply()
            }

            // Arahkan ke MainActivity
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish() // Tutup LoginActivity
        } else {
            Log.e("UpdateUI", "User is null")
        }
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Jika sudah login, langsung ke MainActivity
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish() // Tutup LoginActivity
        }
    }


    companion object {
        private const val TAG = "LoginActivity"
    }
}
