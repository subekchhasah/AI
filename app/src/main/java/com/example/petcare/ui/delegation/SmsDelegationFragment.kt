package com.example.petcare.ui.delegation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petcare.R
import com.example.petcare.databinding.FragmentSmsDelegationBinding
import com.example.petcare.ui.viewmodel.PetViewModel
import com.example.petcare.ui.viewmodel.TaskViewModel
import com.example.petcare.utils.SessionManager
import com.google.android.material.snackbar.Snackbar

class SmsDelegationFragment : Fragment() {

    private var _binding: FragmentSmsDelegationBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()

    private var petId: Long = 1L
    private var generatedSmsText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSmsDelegationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petId = arguments?.getLong("petId", 1L) ?: 1L
        if (petId <= 0) petId = 1L

        val sessionManager = SessionManager(requireContext())
        val ownerName = sessionManager.getUserName()

        petViewModel.getPetById(petId).observe(viewLifecycleOwner) { pet ->
            val petName = pet?.name ?: "Pet"
            val diet = pet?.dietaryPreferences ?: ""
            val allergies = pet?.allergies ?: ""
            val medical = pet?.medicalNotes ?: ""
            updateSmsPreview(petName, diet, allergies, medical, ownerName)
        }

        taskViewModel.allTasks.observe(viewLifecycleOwner) { _ ->
            val pet = petViewModel.getPetById(petId).value
            val petName = pet?.name ?: "Pet"
            val diet = pet?.dietaryPreferences ?: ""
            val allergies = pet?.allergies ?: ""
            val medical = pet?.medicalNotes ?: ""
            updateSmsPreview(petName, diet, allergies, medical, ownerName)
        }

        // Send via SMS Intent
        binding.btnSendSmsIntent.setOnClickListener {
            val phone = binding.etRecipientPhone.text.toString().trim()
            if (phone.isEmpty()) {
                binding.tilRecipientPhone.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            } else {
                binding.tilRecipientPhone.error = null
            }

            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "smsto:$phone".toUri()
                putExtra("sms_body", generatedSmsText)
            }

            try {
                startActivity(smsIntent)
            } catch (_: ActivityNotFoundException) {
                Snackbar.make(binding.root, getString(R.string.sms_no_app_error), Snackbar.LENGTH_LONG).show()
            }
        }

        // Share via Apps Intent (WhatsApp, Messages, Email, etc.)
        binding.btnShareGeneral.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "PetCare Care Plan")
                putExtra(Intent.EXTRA_TEXT, generatedSmsText)
            }
            try {
                startActivity(Intent.createChooser(shareIntent, "Share Pet Care Plan via"))
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Unable to launch share dialog.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSmsPreview(
        petName: String,
        diet: String,
        allergies: String,
        medicalNotes: String,
        ownerName: String
    ) {
        val tasks = taskViewModel.allTasks.value?.filter { it.petId == petId || it.petId == null } ?: emptyList()
        val builder = StringBuilder()
        builder.append("🐾 PetCare — $petName's Care & Feeding Plan 📋\n\n")

        // 1. Checklist & Feeding Schedule
        builder.append("⏰ Daily Checklist & Schedule:\n")
        if (tasks.isNotEmpty()) {
            tasks.forEach { task ->
                val timeStr = try {
                    val sdf24 = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                    val sdf12 = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
                    val date = sdf24.parse(task.scheduledTime)
                    if (date != null) sdf12.format(date) else task.scheduledTime
                } catch (e: Exception) {
                    task.scheduledTime
                }
                builder.append("• $timeStr — ${task.title} (${task.category})\n")
            }
        } else {
            builder.append("• 08:00 AM — Morning Feed & Water 🥣\n")
            builder.append("• 09:00 AM — Morning Walk 🦮\n")
            builder.append("• 06:00 PM — Evening Meal 🍖\n")
            builder.append("• 08:00 PM — Medication / Grooming 💊\n")
        }

        // 2. Diet & Medication Info
        if (diet.isNotEmpty()) {
            builder.append("\n🥣 Dietary Preferences:\n$diet\n")
        }
        if (allergies.isNotEmpty() && allergies != "None") {
            builder.append("\n⚠️ Allergies & Warnings:\n$allergies\n")
        }
        if (medicalNotes.isNotEmpty()) {
            builder.append("\n💊 Medication & Health Notes:\n$medicalNotes\n")
        }

        // 3. Instructions & Emergency Contact
        builder.append("\n💧 General Instructions:\nKeep fresh water available at all times.")
        builder.append("\n\n📱 Owner Contact: $ownerName\nSent via PetCare App 🐾")

        generatedSmsText = builder.toString()
        binding.tvSmsPreviewContent.text = generatedSmsText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
