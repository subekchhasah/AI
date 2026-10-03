package com.example.petcare.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.databinding.FragmentSettingsBinding
import com.example.petcare.utils.SessionManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var selectedPhotoUri: String? = null

    private val selectPhotoLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedPhotoUri = it.toString()
            binding.ivProfilePhoto.setImageURI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())
        binding.etUsername.setText(sessionManager.getUserName())
        binding.etPhone.setText(sessionManager.getPhoneNumber())
        
        sessionManager.getProfilePhotoUri()?.let { uriStr ->
            selectedPhotoUri = uriStr
            try {
                binding.ivProfilePhoto.setImageURI(android.net.Uri.parse(uriStr))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.btnChangePhoto.setOnClickListener {
            selectPhotoLauncher.launch("image/*")
        }

        // Notification Switches
        binding.switchPushNotifs.isChecked = sessionManager.isPushNotificationsEnabled()
        binding.switchPushNotifs.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setPushNotificationsEnabled(isChecked)
        }

        binding.switchDailyDigest.isChecked = sessionManager.isDailyDigestEnabled()
        binding.switchDailyDigest.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setDailyDigestEnabled(isChecked)
        }

        binding.switchSoundVibe.isChecked = sessionManager.isSoundVibrationEnabled()
        binding.switchSoundVibe.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setSoundVibrationEnabled(isChecked)
        }

        // App Preferences
        binding.switchDarkMode.isChecked = sessionManager.isDarkModeEnabled()
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setDarkModeEnabled(isChecked)
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                if (isChecked) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
                else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        if (sessionManager.getWeightUnit() == "lbs") {
            binding.toggleWeightUnit.check(R.id.btn_unit_lbs)
        } else {
            binding.toggleWeightUnit.check(R.id.btn_unit_kg)
        }
        binding.toggleWeightUnit.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val unit = if (checkedId == R.id.btn_unit_lbs) "lbs" else "kg"
                sessionManager.setWeightUnit(unit)
            }
        }

        if (sessionManager.getTempUnit() == "°F") {
            binding.toggleTempUnit.check(R.id.btn_unit_f)
        } else {
            binding.toggleTempUnit.check(R.id.btn_unit_c)
        }
        binding.toggleTempUnit.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val unit = if (checkedId == R.id.btn_unit_f) "°F" else "°C"
                sessionManager.setTempUnit(unit)
            }
        }

        // Save Profile Button
        binding.btnSaveSettings.setOnClickListener {
            val newUsername = binding.etUsername.text.toString().trim()
            val newPhone = binding.etPhone.text.toString().trim()
            val newPassword = binding.etPassword.text.toString().trim()

            if (newUsername.isNotEmpty()) {
                sessionManager.updateProfile(newUsername, sessionManager.getUserEmail())
            }

            if (newPhone.isNotEmpty()) {
                sessionManager.setPhoneNumber(newPhone)
            }

            if (newPassword.isNotEmpty()) {
                sessionManager.savePassword(newPassword)
            }

            selectedPhotoUri?.let { uri ->
                sessionManager.saveProfilePhotoUri(uri)
            }

            com.google.android.material.snackbar.Snackbar.make(
                binding.root,
                "Profile updated successfully!",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }

        // Data & Privacy Actions
        binding.btnExportData.setOnClickListener {
            com.google.android.material.snackbar.Snackbar.make(
                binding.root,
                "Exporting medical & care records to CSV...",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }

        binding.btnClearCache.setOnClickListener {
            requireContext().cacheDir.deleteRecursively()
            com.google.android.material.snackbar.Snackbar.make(
                binding.root,
                "Cache & temporary files cleared!",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }

        // Support Actions
        binding.btnSupport.setOnClickListener {
            com.google.android.material.snackbar.Snackbar.make(
                binding.root,
                "Help & Support: support@petcareapp.com",
                com.google.android.material.snackbar.Snackbar.LENGTH_LONG
            ).show()
        }

        binding.btnPrivacyPolicy.setOnClickListener {
            com.google.android.material.snackbar.Snackbar.make(
                binding.root,
                "Privacy Policy & Terms: https://petcareapp.com/privacy",
                com.google.android.material.snackbar.Snackbar.LENGTH_LONG
            ).show()
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            findNavController().navigate(R.id.action_settings_to_welcome)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
