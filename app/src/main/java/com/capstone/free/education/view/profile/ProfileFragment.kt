package com.capstone.free.education.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstone.free.education.data.pref.dataStore
import com.capstone.free.education.view.setting.SettingViewModel
import com.capstone.free.education.databinding.FragmentProfileBinding
import com.capstone.free.education.view.ViewModelFactory
import com.capstone.free.education.view.login.LoginActivity
import com.capstone.free.education.view.main.MainViewModel
import com.capstone.free.education.view.setting.SettingPreferences
import kotlinx.coroutines.launch

// ProfileFragment.kt
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding // Deklarasi binding

    private val viewModel by viewModels<MainViewModel> {
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        ViewModelFactory.getInstance(requireContext(), pref)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi binding di sini
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root // Mengembalikan root view dari binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction() // Pastikan setupAction dipanggil setelah binding diinisialisasi
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            // Logout logic dijalankan di background thread
            lifecycleScope.launch {
                // Simpan status tema saat ini sebelum logout
                val settingViewModel = ViewModelProvider(
                    requireActivity(),
                    ViewModelFactory.getInstance(requireContext(), SettingPreferences.getInstance(requireContext().dataStore))
                ).get(SettingViewModel::class.java)

                settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkMode ->
                    // Simpan tema saat ini
                    lifecycleScope.launch {
                        settingViewModel.saveThemeSetting(isDarkMode)
                    }

                    // Logout dan navigasi ke LoginActivity setelah tema disimpan
                    viewModel.logout() // Logout dari viewModel
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish() // Finish fragment setelah logout

                    // Nonaktifkan animasi transisi untuk logout cepat
                    requireActivity().overridePendingTransition(0, 0)
                }
            }
        }
    }
}



