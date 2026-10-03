package com.example.petcare.ui.expenses

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
import com.example.petcare.R
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentAddEditExpenseBinding
import com.example.petcare.ui.viewmodel.ExpenseViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditExpenseFragment : Fragment() {

    private var _binding: FragmentAddEditExpenseBinding? = null
    private val binding get() = _binding!!

    private val expenseViewModel: ExpenseViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var expenseId: Long = -1L
    private var selectedPetId: Long? = null
    private var petList: List<Pet> = emptyList()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseId = arguments?.getLong("expenseId", -1L) ?: -1L
        val argPetId = arguments?.getLong("petId", -1L) ?: -1L
        if (argPetId > 0) {
            selectedPetId = argPetId
        }

        // Setup Pet Dropdown
        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            val nonNullPets = pets ?: emptyList()
            petList = nonNullPets
            val petOptions = mutableListOf("General / All Pets 🐾")
            petOptions.addAll(nonNullPets.map { "${it.name} (${it.species})" })

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, petOptions)
            binding.actvExpensePet.setAdapter(adapter)

            if (selectedPetId != null) {
                val matchedIndex = nonNullPets.indexOfFirst { it.petId == selectedPetId }
                if (matchedIndex >= 0) {
                    binding.actvExpensePet.setText(petOptions[matchedIndex + 1], false)
                } else {
                    binding.actvExpensePet.setText(petOptions[0], false)
                }
            } else {
                binding.actvExpensePet.setText(petOptions[0], false)
            }
        }

        binding.actvExpensePet.setOnItemClickListener { _, _, position, _ ->
            selectedPetId = if (position == 0 || petList.isEmpty()) {
                null
            } else {
                petList.getOrNull(position - 1)?.petId
            }
        }

        // Default Date (yyyy-MM-dd)
        val defaultDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (binding.etExpenseDate.text.isNullOrEmpty()) {
            binding.etExpenseDate.setText(defaultDate)
        }

        // Default Time (12-hour AM/PM)
        val defaultTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        if (binding.etExpenseTime.text.isNullOrEmpty()) {
            binding.etExpenseTime.setText(defaultTime)
        }

        // Date Picker Dialog
        binding.etExpenseDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    binding.etExpenseDate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Time Picker Dialog
        binding.etExpenseTime.setOnClickListener {
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
                    binding.etExpenseTime.setText(formattedTime)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        if (expenseId > 0) {
            binding.tvExpenseFormTitle.text = "Edit Expense 💰"
            expenseViewModel.getExpenseById(expenseId).observe(viewLifecycleOwner) { exp ->
                exp?.let {
                    binding.etExpenseDesc.setText(it.description)
                    binding.etExpenseAmount.setText(it.amount.toString())
                    binding.etExpenseCategory.setText(it.category)
                    binding.etExpenseDate.setText(it.date)
                    binding.etExpenseNotes.setText(it.notes)
                    selectedPetId = it.petId
                }
            }
        }

        binding.btnSaveExpense.setOnClickListener {
            val desc = binding.etExpenseDesc.text.toString().trim()
            val amountStr = binding.etExpenseAmount.text.toString().trim()
            val category = binding.etExpenseCategory.text.toString().trim().ifEmpty { "Food" }
            val date = binding.etExpenseDate.text.toString().trim().ifEmpty { defaultDate }
            val notes = binding.etExpenseNotes.text.toString().trim()

            if (desc.isEmpty()) {
                binding.tilExpenseDesc.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            } else {
                binding.tilExpenseDesc.error = null
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0.0) {
                binding.tilExpenseAmount.error = "Please enter a valid amount"
                return@setOnClickListener
            } else {
                binding.tilExpenseAmount.error = null
            }

            expenseViewModel.saveExpense(
                id = expenseId,
                petId = selectedPetId,
                category = category,
                description = desc,
                amount = amount,
                date = date,
                notes = notes
            ) {
                Snackbar.make(binding.root, "Expense saved successfully! 💰", Snackbar.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
