package com.example.petcare.ui.pets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.databinding.FragmentPetDetailBinding
import com.example.petcare.ui.viewmodel.PetViewModel

class PetDetailFragment : Fragment() {

    private var _binding: FragmentPetDetailBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private var petId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petId = arguments?.getLong("petId") ?: -1L

        if (petId != -1L) {
            petViewModel.getPetById(petId).observe(viewLifecycleOwner) { pet ->
                pet?.let {
                    binding.tvDetailName.text = it.name
                    binding.tvDetailInitial.text = it.name.trim().firstOrNull()?.toString()?.uppercase() ?: "P"
                    binding.tvDetailSpeciesBreed.text =
                        getString(R.string.pet_species_breed_format, it.species, it.breed)
                    binding.tvDetailWeight.text = getString(R.string.pet_weight_format, it.weight)
                    binding.tvDetailDiet.text = it.dietaryPreferences.ifEmpty { "None specified" }
                    binding.tvDetailAllergies.text = it.allergies.ifEmpty { "None" }
                    binding.tvDetailMedicalNotes.text = it.medicalNotes.ifEmpty { "No medical notes recorded." }
                    binding.tvDetailToysNotes.text = "${it.favouriteToys}\n${it.notes}".trim()

                    // Pet Age in Pet Years calculation
                    val multiplier = if (it.species.lowercase().contains("cat")) 5 else 7
                    val estimatedAgeYears = try {
                        val dob = java.time.LocalDate.parse(it.dateOfBirth, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
                        java.time.Period.between(dob, java.time.LocalDate.now()).years
                    } catch (e: Exception) {
                        2 // fallback if dateOfBirth is invalid or empty
                    }
                    val petYears = estimatedAgeYears * multiplier
                    binding.tvDetailAgePetYears.text = "Age: $estimatedAgeYears yrs ($petYears in pet years) 🎂"

                    it.imageUri?.let { uriStr ->
                        com.example.petcare.utils.ImageLoaderUtils.loadPetImage(binding.ivDetailPhoto, uriStr)
                    }
                }
            }
        }

        binding.btnDetailEdit.setOnClickListener {
            val bundle = Bundle().apply { putLong("petId", petId) }
            findNavController().navigate(R.id.action_petDetail_to_editPet, bundle)
        }

        binding.btnDetailDelegateSms.setOnClickListener {
            val bundle = Bundle().apply { putLong("petId", petId) }
            findNavController().navigate(R.id.action_petDetail_to_smsDelegation, bundle)
        }

        binding.btnEmergencyCall.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0800111999"))
            startActivity(dialIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
