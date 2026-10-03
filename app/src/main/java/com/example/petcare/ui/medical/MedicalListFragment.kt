package com.example.petcare.ui.medical

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
import com.example.petcare.data.local.entities.MedicalRecord
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentMedicalListBinding
import com.example.petcare.databinding.ItemMedicalCardBinding
import com.example.petcare.ui.viewmodel.MedicalViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar

class MedicalListFragment : Fragment() {

    private var _binding: FragmentMedicalListBinding? = null
    private val binding get() = _binding!!

    private val medicalViewModel: MedicalViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var petList: List<Pet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicalListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddMedical.setOnClickListener {
            findNavController().navigate(R.id.action_medicalList_to_addMedical)
        }

        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petList = pets ?: emptyList()
            refreshMedicalRecords()
        }

        medicalViewModel.allMedicalRecords.observe(viewLifecycleOwner) { _ ->
            refreshMedicalRecords()
        }
    }

    private fun refreshMedicalRecords() {
        val records = medicalViewModel.allMedicalRecords.value ?: emptyList()
        if (records.isEmpty()) {
            binding.rvMedical.visibility = View.GONE
            binding.layoutEmptyMedical.visibility = View.VISIBLE
        } else {
            binding.rvMedical.visibility = View.VISIBLE
            binding.layoutEmptyMedical.visibility = View.GONE
            binding.rvMedical.adapter = MedicalAdapter(
                records = records,
                pets = petList,
                onEditClick = { record ->
                    val bundle = Bundle().apply { putLong("medicalRecordId", record.medicalRecordId) }
                    findNavController().navigate(R.id.action_medicalList_to_addMedical, bundle)
                },
                onDeleteClick = { record -> showDeleteDialog(record) }
            )
        }
    }

    private fun showDeleteDialog(record: MedicalRecord) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Medical Record 🏥")
            .setMessage("Are you sure you want to delete '${record.title}'?")
            .setPositiveButton(R.string.btn_delete) { _, _ ->
                medicalViewModel.deleteRecord(record) {
                    Snackbar.make(binding.root, "'${record.title}' deleted.", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class MedicalAdapter(
        private val records: List<MedicalRecord>,
        private val pets: List<Pet>,
        private val onEditClick: (MedicalRecord) -> Unit,
        private val onDeleteClick: (MedicalRecord) -> Unit
    ) : RecyclerView.Adapter<MedicalAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemMedicalCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemMedicalCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = records[position]
            holder.binding.tvMedicalItemTitle.text = record.title

            val matchedPet = pets.find { it.petId == record.petId }
            val petLabel = if (matchedPet != null) "${matchedPet.name} (${matchedPet.breed})" else "General / All Pets"
            holder.binding.tvMedicalPetBadge.text = "🐾 $petLabel"

            val typePrefix = when (record.recordType.lowercase()) {
                "vaccination" -> "💉 "
                "medication" -> "💊 "
                "health check", "checkup" -> "🩺 "
                else -> "📋 "
            }
            holder.binding.tvMedicalTypeBadge.text = "$typePrefix${record.recordType}"
            holder.binding.tvMedicalDate.text = "📅 ${record.date}"

            val vetText = record.veterinarian.ifEmpty { "Vet" }
            val clinicText = record.clinic.ifEmpty { "Clinic" }
            holder.binding.tvMedicalVetClinic.text = "👨‍⚕️ $vetText • $clinicText"

            if (record.notes.isNotEmpty()) {
                holder.binding.tvMedicalNotes.visibility = View.VISIBLE
                holder.binding.tvMedicalNotes.text = record.notes
            } else {
                holder.binding.tvMedicalNotes.visibility = View.GONE
            }

            holder.binding.btnMedicalEdit.setOnClickListener { onEditClick(record) }
            holder.binding.btnMedicalDelete.setOnClickListener { onDeleteClick(record) }
        }

        override fun getItemCount(): Int = records.size
    }
}
