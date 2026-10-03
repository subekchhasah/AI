package com.example.petcare.ui.tasks

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentAddEditTaskBinding
import com.example.petcare.ui.viewmodel.PetViewModel
import com.example.petcare.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditTaskFragment : Fragment() {

    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var taskId: Long = -1L
    private var selectedPetId: Long? = 1L
    private var petList: List<Pet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskId = arguments?.getLong("taskId", -1L) ?: -1L
        val argPetId = arguments?.getLong("petId", -1L) ?: -1L
        if (argPetId > 0) {
            selectedPetId = argPetId
        }

        // Setup Pet Selection Dropdown
        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            val nonNullPets = pets ?: emptyList()
            petList = nonNullPets
            val petNames = nonNullPets.map { "${it.name} (${it.species})" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, petNames)
            binding.actvSelectPet.setAdapter(adapter)

            if (nonNullPets.isNotEmpty()) {
                val defaultIndex = nonNullPets.indexOfFirst { it.petId == selectedPetId }.let { if (it >= 0) it else 0 }
                selectedPetId = nonNullPets[defaultIndex].petId
                binding.actvSelectPet.setText(petNames[defaultIndex], false)
            }
        }

        binding.actvSelectPet.setOnItemClickListener { _, _, position, _ ->
            if (position in petList.indices) {
                selectedPetId = petList[position].petId
            }
        }

        // Default Time (HH:mm)
        val defaultTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        if (binding.etTaskTime.text.isNullOrEmpty()) {
            binding.etTaskTime.setText(defaultTime)
        }

        binding.etTaskTime.isFocusable = false
        binding.etTaskTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    val formatted = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                    binding.etTaskTime.setText(formatted)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        if (taskId > 0) {
            binding.tvTaskFormTitle.text = getString(R.string.title_edit_task)
            taskViewModel.getTaskById(taskId).observe(viewLifecycleOwner) { task ->
                task?.let {
                    binding.etTaskTitle.setText(it.title)
                    binding.etTaskCategory.setText(it.category)
                    binding.etTaskTime.setText(it.scheduledTime)
                    binding.switchReminder.isChecked = it.reminderEnabled
                    if (it.petId != null) {
                        selectedPetId = it.petId
                        val matchedPet = petList.find { p -> p.petId == it.petId }
                        if (matchedPet != null) {
                            binding.actvSelectPet.setText("${matchedPet.name} (${matchedPet.species})", false)
                        }
                    }
                }
            }
        }

        binding.btnSaveTask.setOnClickListener {
            val title = binding.etTaskTitle.text.toString().trim()
            val category = binding.etTaskCategory.text.toString().trim()
            val time = binding.etTaskTime.text.toString().trim()
            val reminder = binding.switchReminder.isChecked

            if (title.isEmpty()) {
                binding.tilTaskTitle.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            } else {
                binding.tilTaskTitle.error = null
            }

            taskViewModel.saveTask(
                taskId = taskId,
                petId = selectedPetId ?: 1L,
                routineId = null,
                title = title,
                description = "",
                category = category.ifEmpty { "Feeding" },
                date = taskViewModel.todayDateStr,
                time = time.ifEmpty { defaultTime },
                frequency = "Daily",
                supplies = "",
                notes = "",
                priority = "Normal",
                reminderEnabled = reminder
            ) {
                Toast.makeText(requireContext(), "Care task saved successfully! 🐾", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
