package com.example.petcare.ui.locations

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
import com.example.petcare.databinding.FragmentAddEditLocationBinding
import com.example.petcare.ui.viewmodel.LocationViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar

class AddEditLocationFragment : Fragment() {

    private var _binding: FragmentAddEditLocationBinding? = null
    private val binding get() = _binding!!

    private val locationViewModel: LocationViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var locationId: Long = -1L
    private var selectedPetId: Long? = null
    private var petList: List<Pet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationId = arguments?.getLong("locationId", -1L) ?: -1L

        // Setup Pet Selection Dropdown
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

        if (locationId > 0) {
            binding.tvLocationFormTitle.text = "Edit Pet Location 📍"
            locationViewModel.getLocationById(locationId).observe(viewLifecycleOwner) { loc ->
                loc?.let {
                    binding.etLocationName.setText(it.name)
                    binding.etLocationType.setText(it.type)
                    binding.etLocationAddress.setText(it.address)
                    binding.etLocationLat.setText(it.latitude.toString())
                    binding.etLocationLng.setText(it.longitude.toString())
                    binding.etLocationNotes.setText(it.notes)
                    selectedPetId = it.petId

                    val matchedIndex = petList.indexOfFirst { p -> p.petId == it.petId }
                    if (matchedIndex >= 0) {
                        binding.actvSelectPet.setText("${petList[matchedIndex].name} (${petList[matchedIndex].species} • ${petList[matchedIndex].breed})", false)
                    }
                }
            }
        }

        binding.btnSaveLocation.setOnClickListener {
            val name = binding.etLocationName.text.toString().trim()
            val type = binding.etLocationType.text.toString().trim()
            val address = binding.etLocationAddress.text.toString().trim()
            val latStr = binding.etLocationLat.text.toString().trim()
            val lngStr = binding.etLocationLng.text.toString().trim()
            val notes = binding.etLocationNotes.text.toString().trim()

            if (name.isEmpty()) {
                binding.tilLocationName.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            } else {
                binding.tilLocationName.error = null
            }

            val lat = latStr.toDoubleOrNull() ?: 51.5074
            val lng = lngStr.toDoubleOrNull() ?: -0.1278

            locationViewModel.saveLocation(
                id = locationId,
                petId = selectedPetId,
                name = name,
                type = type.ifEmpty { "Veterinary Clinic" },
                lat = lat,
                lng = lng,
                address = address.ifEmpty { "London, UK" },
                notes = notes
            ) {
                Snackbar.make(binding.root, "Pet location saved successfully! 📍", Snackbar.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
