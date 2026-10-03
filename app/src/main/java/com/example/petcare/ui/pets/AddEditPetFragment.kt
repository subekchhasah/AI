package com.example.petcare.ui.pets

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.databinding.FragmentAddEditPetBinding
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar

class AddEditPetFragment : Fragment() {

    private var _binding: FragmentAddEditPetBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private var petId: Long = -1L
    private var selectedMediaUri: String? = null

    private val mediaPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedMediaUri = it.toString()
            binding.tvMediaPath.text = "Selected: ${it.lastPathSegment ?: "Media"}"
            binding.ivCameraPlaceholder.visibility = View.GONE
            binding.ivPetPreview.visibility = View.VISIBLE
            binding.ivPetPreview.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petId = arguments?.getLong("petId", -1L) ?: -1L

        if (petId > 0) {
            binding.tvAddEditTitle.text = getString(R.string.title_edit_pet)
            petViewModel.getPetById(petId).observe(viewLifecycleOwner) { pet ->
                pet?.let {
                    binding.etPetName.setText(it.name)
                    binding.etPetSpecies.setText(it.species)
                    binding.etPetBreed.setText(it.breed)
                    binding.etPetWeight.setText(it.weight.toString())
                    binding.etPetDiet.setText(it.dietaryPreferences)
                    it.imageUri?.let { uriStr ->
                        selectedMediaUri = uriStr
                        binding.tvMediaPath.text = "Media Attached"
                        binding.ivCameraPlaceholder.visibility = View.GONE
                        com.example.petcare.utils.ImageLoaderUtils.loadPetImage(binding.ivPetPreview, uriStr)
                    }
                }
            }
        }

        // Media Picker Listener (Images & Videos)
        val pickMediaAction = View.OnClickListener {
            mediaPickerLauncher.launch("*/*")
        }

        binding.btnPickMedia.setOnClickListener(pickMediaAction)
        binding.cardSelectMedia.setOnClickListener(pickMediaAction)

        binding.btnSavePet.setOnClickListener {
            val name = binding.etPetName.text.toString().trim()
            val species = binding.etPetSpecies.text.toString().trim()
            val breed = binding.etPetBreed.text.toString().trim()
            val weightStr = binding.etPetWeight.text.toString().trim()
            val diet = binding.etPetDiet.text.toString().trim()

            if (name.isEmpty()) {
                binding.tilPetName.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }

            val weight = weightStr.toDoubleOrNull() ?: 0.0

            petViewModel.savePet(
                petId = petId,
                name = name,
                species = species.ifEmpty { "Dog" },
                breed = breed.ifEmpty { "Mixed" },
                dob = "2023-01-01",
                gender = "Male",
                weight = weight,
                diet = diet,
                allergies = "",
                vaccinations = "",
                medicalNotes = "",
                favouriteToys = "",
                notes = "",
                imageUri = selectedMediaUri
            ) {
                Snackbar.make(binding.root, getString(R.string.msg_saved_successfully), Snackbar.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
