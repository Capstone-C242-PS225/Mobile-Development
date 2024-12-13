package com.capstone.free.education.view.profile

import android.content.Context
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
import com.capstone.free.education.view.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<MainViewModel> {
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        ViewModelFactory.getInstance(requireContext(), pref)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                val settingViewModel = ViewModelProvider(
                    requireActivity(),
                    ViewModelFactory.getInstance(requireContext(), SettingPreferences.getInstance(requireContext().dataStore))
                ).get(SettingViewModel::class.java)

                settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkMode ->

                    lifecycleScope.launch {
                        settingViewModel.saveThemeSetting(isDarkMode)
                    }

                    val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        clear()
                        apply()
                    }

                    FirebaseAuth.getInstance().signOut()

                    viewModel.logout()

                    startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                    requireActivity().finish()

                    requireActivity().overridePendingTransition(0, 0)
                }
            }
        }
    }
}




