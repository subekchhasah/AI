package com.example.petcare.ui.pets

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentPetListBinding
import com.example.petcare.databinding.ItemPetCardBinding
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar

class PetListFragment : Fragment() {

    private var _binding: FragmentPetListBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddPet.setOnClickListener {
            findNavController().navigate(R.id.action_petList_to_addPet)
        }

        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            if (pets.isEmpty()) {
                binding.layoutEmptyPets.visibility = View.VISIBLE
                binding.rvPets.visibility = View.GONE
            } else {
                binding.layoutEmptyPets.visibility = View.GONE
                binding.rvPets.visibility = View.VISIBLE
                binding.rvPets.adapter = PetAdapter(
                    pets = pets,
                    onItemClick = { pet ->
                        val bundle = Bundle().apply { putLong("petId", pet.petId) }
                        findNavController().navigate(R.id.action_petList_to_petDetail, bundle)
                    },
                    onEditClick = { pet ->
                        val bundle = Bundle().apply { putLong("petId", pet.petId) }
                        findNavController().navigate(R.id.action_petList_to_addPet, bundle)
                    },
                    onDeleteClick = { pet -> showDeleteDialog(pet) },
                )
            }
        }
    }

    private fun showDeleteDialog(pet: Pet) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_delete_pet_title)
            .setMessage(getString(R.string.dialog_delete_pet_msg, pet.name))
            .setPositiveButton(R.string.btn_delete) { _, _ ->
                petViewModel.deletePet(pet) {
                    Snackbar.make(binding.root, "${pet.name} deleted.", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class PetAdapter(
        private val pets: List<Pet>,
        private val onItemClick: (Pet) -> Unit,
        private val onEditClick: (Pet) -> Unit,
        private val onDeleteClick: (Pet) -> Unit
    ) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemPetCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemPetCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val pet = pets[position]
            holder.binding.tvPetName.text = pet.name
            holder.binding.tvPetSpeciesBreed.text =
                holder.itemView.context.getString(R.string.pet_species_breed_format, pet.species, pet.breed)
            holder.binding.tvPetWeight.text =
                holder.itemView.context.getString(R.string.pet_weight_format, pet.weight)
            holder.binding.tvPetListInitial.text = pet.name.trim().firstOrNull()?.toString()?.uppercase() ?: "P"

            pet.imageUri?.let { uriStr ->
                com.example.petcare.utils.ImageLoaderUtils.loadPetImage(holder.binding.ivPetImage, uriStr)
            }

            holder.itemView.setOnClickListener { onItemClick(pet) }
            holder.binding.btnPetEdit.setOnClickListener { onEditClick(pet) }
            holder.binding.btnPetDelete.setOnClickListener { onDeleteClick(pet) }
        }

        override fun getItemCount(): Int = pets.size
    }
}
