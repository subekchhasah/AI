package com.example.petcare.ui.locations

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
import com.example.petcare.data.local.entities.PetLocation
import com.example.petcare.databinding.FragmentLocationListBinding
import com.example.petcare.databinding.ItemLocationCardBinding
import com.example.petcare.ui.viewmodel.LocationViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar

class LocationListFragment : Fragment() {

    private var _binding: FragmentLocationListBinding? = null
    private val binding get() = _binding!!

    private val locationViewModel: LocationViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var petsList: List<Pet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLocationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenMap.setOnClickListener {
            findNavController().navigate(R.id.action_locationList_to_map)
        }

        binding.fabAddLocation.setOnClickListener {
            findNavController().navigate(R.id.action_locationList_to_addLocation)
        }

        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petsList = pets ?: emptyList()
            refreshLocations()
        }

        locationViewModel.allLocations.observe(viewLifecycleOwner) { _ ->
            refreshLocations()
        }
    }

    private fun refreshLocations() {
        val locations = locationViewModel.allLocations.value ?: emptyList()
        if (locations.isEmpty()) {
            binding.rvLocations.visibility = View.GONE
        } else {
            binding.rvLocations.visibility = View.VISIBLE
            binding.rvLocations.adapter = LocationAdapter(
                locations = locations,
                pets = petsList,
                onEditClick = { loc ->
                    val bundle = Bundle().apply { putLong("locationId", loc.locationId) }
                    findNavController().navigate(R.id.action_locationList_to_addLocation, bundle)
                },
                onDeleteClick = { loc -> showDeleteDialog(loc) }
            )
        }
    }

    private fun showDeleteDialog(loc: PetLocation) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Location 📍")
            .setMessage("Are you sure you want to delete '${loc.name}'?")
            .setPositiveButton(R.string.btn_delete) { _, _ ->
                locationViewModel.deleteLocation(loc) {
                    Snackbar.make(binding.root, "'${loc.name}' deleted.", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class LocationAdapter(
        private val locations: List<PetLocation>,
        private val pets: List<Pet>,
        private val onEditClick: (PetLocation) -> Unit,
        private val onDeleteClick: (PetLocation) -> Unit
    ) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemLocationCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemLocationCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val loc = locations[position]
            holder.binding.tvLocationName.text = loc.name
            holder.binding.tvLocationTypeAddress.text =
                holder.itemView.context.getString(R.string.location_type_address_format, loc.type, loc.address.ifEmpty { "London, UK" })
            holder.binding.tvLocationCoords.text =
                holder.itemView.context.getString(R.string.location_coords_format, loc.latitude, loc.longitude)

            // Match assigned Pet / Dog
            val matchedPet = pets.find { it.petId == loc.petId }
            val petLabel = if (matchedPet != null) "${matchedPet.name} (${matchedPet.breed})" else "General / All Pets"
            holder.binding.tvLocationPetBadge.text = "🐾 Pet: $petLabel"

            if (loc.notes.isNotEmpty()) {
                holder.binding.tvLocationNotes.visibility = View.VISIBLE
                holder.binding.tvLocationNotes.text = loc.notes
            } else {
                holder.binding.tvLocationNotes.visibility = View.GONE
            }

            holder.binding.btnLocationEdit.setOnClickListener { onEditClick(loc) }
            holder.binding.btnLocationDelete.setOnClickListener { onDeleteClick(loc) }
        }

        override fun getItemCount(): Int = locations.size
    }
}
