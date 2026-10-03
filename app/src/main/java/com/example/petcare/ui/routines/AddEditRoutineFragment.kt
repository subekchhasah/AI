package com.example.petcare.ui.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.databinding.FragmentAddEditRoutineBinding
import com.example.petcare.ui.viewmodel.RoutineViewModel
import com.google.android.material.snackbar.Snackbar

class AddEditRoutineFragment : Fragment() {

    private var _binding: FragmentAddEditRoutineBinding? = null
    private val binding get() = _binding!!

    private val routineViewModel: RoutineViewModel by viewModels()
    private var petId: Long = 1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petId = arguments?.getLong("petId", 1L) ?: 1L

        binding.btnSaveRoutine.setOnClickListener {
            val name = binding.etRoutineName.text.toString().trim()
            if (name.isNotEmpty()) {
                routineViewModel.saveRoutine(
                    routineId = -1L,
                    petId = petId,
                    name = name,
                    description = "",
                    frequency = "Daily",
                    startDate = "2026-09-09",
                    endDate = "",
                    notes = ""
                ) {
                    Snackbar.make(binding.root, "Routine saved successfully.", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
