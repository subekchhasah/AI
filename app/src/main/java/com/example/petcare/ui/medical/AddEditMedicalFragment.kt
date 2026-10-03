package com.example.petcare.ui.medical

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentAddEditMedicalBinding
import com.example.petcare.ui.viewmodel.MedicalViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditMedicalFragment : Fragment() {

    private var _binding: FragmentAddEditMedicalBinding? = null
    private val binding get() = _binding!!

    private val medicalViewModel: MedicalViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var medicalRecordId: Long = -1L
    private var selectedPetId: Long? = null
    private var petList: List<Pet> = emptyList()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditMedicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passedPetId = arguments?.getLong("petId", -1L) ?: -1L
        if (passedPetId > 0) {
            selectedPetId = passedPetId
        }

        medicalRecordId = arguments?.getLong("medicalRecordId", -1L) ?: -1L

        // Populate Pet Selection Dropdown
        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            val nonNullPets = pets ?: emptyList()
            petList = nonNullPets

            val petOptions = mutableListOf("General / All Pets 🐾")
            petOptions.addAll(nonNullPets.map { "${it.name} (${it.species} • ${it.breed})" })

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, petOptions)
            binding.actvSelectPet.setAdapter(adapter)

            if (selectedPetId != null) {
                val matchedIndex = nonNullPets.indexOfFirst { it.petId == selectedPetId }
                if (matchedIndex >= 0) {
                    binding.actvSelectPet.setText(petOptions[matchedIndex + 1], false)
                } else {
                    binding.actvSelectPet.setText(petOptions[0], false)
                }
            } else if (nonNullPets.isNotEmpty()) {
                selectedPetId = nonNullPets.first().petId
                binding.actvSelectPet.setText(petOptions[1], false)
            } else {
                binding.actvSelectPet.setText(petOptions[0], false)
            }
        }

        binding.actvSelectPet.setOnItemClickListener { _, _, position, _ ->
            selectedPetId = if (position == 0 || petList.isEmpty()) {
                null
            } else {
                petList.getOrNull(position - 1)?.petId
            }
        }

        // Default Date (yyyy-MM-dd)
        val defaultDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.etMedicalDate.setText(defaultDate)

        // Default Time (12-hour AM/PM)
        val defaultTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        binding.etMedicalTime.setText(defaultTime)

        // Date Picker Dialog
        binding.etMedicalDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    binding.etMedicalDate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Time Picker Dialog
        binding.etMedicalTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    val timeCal = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                    }
                    val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(timeCal.time)
                    binding.etMedicalTime.setText(formattedTime)
                },
                hour,
                minute,
                false // 12-hour format with AM/PM picker
            )
            timePickerDialog.show()
        }

        if (medicalRecordId > 0) {
            binding.tvMedicalFormTitle.text = "Edit Medical Record 🏥"
            medicalViewModel.getRecordById(medicalRecordId).observe(viewLifecycleOwner) { record ->
                record?.let {
                    binding.etMedicalTitle.setText(it.title)
                    binding.etMedicalType.setText(it.recordType)
                    binding.etMedicalVet.setText(it.veterinarian)
                    binding.etMedicalClinic.setText(it.clinic)
                    binding.etMedicalNotes.setText(it.notes)
                    selectedPetId = it.petId

                    val matchedPet = petList.find { p -> p.petId == it.petId }
                    if (matchedPet != null) {
                        binding.actvSelectPet.setText("${matchedPet.name} (${matchedPet.species} • ${matchedPet.breed})", false)
                    }
                }
            }
        }

        binding.btnSaveMedical.setOnClickListener {
            val title = binding.etMedicalTitle.text.toString().trim()
            val type = binding.etMedicalType.text.toString().trim().ifEmpty { "Vaccination" }
            val date = binding.etMedicalDate.text.toString().trim().ifEmpty { defaultDate }
            val time = binding.etMedicalTime.text.toString().trim().ifEmpty { defaultTime }
            val vet = binding.etMedicalVet.text.toString().trim()
            val clinic = binding.etMedicalClinic.text.toString().trim()
            val notes = binding.etMedicalNotes.text.toString().trim()

            if (title.isEmpty()) {
                binding.tilMedicalTitle.error = "Please enter a record title"
                return@setOnClickListener
            } else {
                binding.tilMedicalTitle.error = null
            }

            val dateTimeCombined = "$date at $time"

            medicalViewModel.saveMedicalRecord(
                id = medicalRecordId,
                petId = selectedPetId,
                type = type,
                title = title,
                date = dateTimeCombined,
                vet = vet,
                clinic = clinic,
                notes = notes
            ) { resultId ->
                if (resultId != -1L) {
                    Snackbar.make(binding.root, "Medical record saved successfully! 🏥", Snackbar.LENGTH_SHORT).show()
                }
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
