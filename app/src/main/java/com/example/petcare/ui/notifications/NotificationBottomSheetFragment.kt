package com.example.petcare.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.databinding.FragmentNotificationBottomSheetBinding
import com.example.petcare.databinding.ItemNotificationCardBinding
import com.example.petcare.ui.viewmodel.LocationViewModel
import com.example.petcare.ui.viewmodel.MedicalViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.example.petcare.ui.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

data class AppNotification(
    val id: String,
    val emoji: String,
    val title: String,
    val body: String,
    val tag: String,
    val timeText: String,
    val destinationId: Int? = null,
    val args: Bundle? = null
)

class NotificationBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentNotificationBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val medicalViewModel: MedicalViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    private var isCleared = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClearNotifications.setOnClickListener {
            isCleared = true
            updateNotificationList(emptyList())
        }

        observeData()
    }

    private fun observeData() {
        petViewModel.pets.observe(viewLifecycleOwner) { _ -> buildNotifications() }
        taskViewModel.allTasks.observe(viewLifecycleOwner) { _ -> buildNotifications() }
        medicalViewModel.allMedicalRecords.observe(viewLifecycleOwner) { _ -> buildNotifications() }
        locationViewModel.allLocations.observe(viewLifecycleOwner) { _ -> buildNotifications() }
    }

    private fun buildNotifications() {
        if (isCleared) {
            updateNotificationList(emptyList())
            return
        }

        val pets = petViewModel.pets.value ?: emptyList()
        val tasks = taskViewModel.allTasks.value ?: emptyList()
        val medicals = medicalViewModel.allMedicalRecords.value ?: emptyList()
        val locations = locationViewModel.allLocations.value ?: emptyList()

        val list = mutableListOf<AppNotification>()

        // 1. Feeding & Task Notifications
        tasks.forEach { task ->
            val matchedPet = pets.find { it.petId == task.petId }
            val petName = matchedPet?.name ?: "Your Pet"
            val species = matchedPet?.species ?: "Pet"

            val isFeeding = task.category.equals("Feeding", ignoreCase = true)
            val isMedication = task.category.equals("Medication", ignoreCase = true)

            val emoji = when {
                isFeeding -> "🥣"
                isMedication -> "💊"
                task.category.equals("Exercise", ignoreCase = true) -> "🎾"
                task.category.equals("Grooming", ignoreCase = true) -> "✂️"
                else -> "⏰"
            }

            val title = when {
                isFeeding -> "Time to feed $petName! 🥣"
                isMedication -> "Medication Due: $petName 💊"
                else -> "Scheduled Care: ${task.title}"
            }

            val body = if (task.description.isNotEmpty()) {
                "$petName ($species) - ${task.description} at ${task.scheduledTime}"
            } else {
                "$petName needs ${task.title} at ${task.scheduledTime}"
            }

            val tagText = when {
                isFeeding -> "🥣 Feeding Reminder"
                isMedication -> "💊 Medication Alert"
                else -> "📅 Care Task"
            }

            list.add(
                AppNotification(
                    id = "task_${task.taskId}",
                    emoji = emoji,
                    title = title,
                    body = body,
                    tag = tagText,
                    timeText = task.scheduledTime,
                    destinationId = R.id.checklistFragment
                )
            )
        }

        // 2. New Pet Added Notifications
        pets.forEach { pet ->
            list.add(
                AppNotification(
                    id = "pet_${pet.petId}",
                    emoji = "🐾",
                    title = "New Pet Added: ${pet.name}! 🐾",
                    body = "${pet.name} (${pet.species} • ${pet.breed}) was successfully added to PetCare family.",
                    tag = "🐾 New Pet Enrolled",
                    timeText = "Active",
                    destinationId = R.id.petDetailFragment,
                    args = Bundle().apply { putLong("petId", pet.petId) }
                )
            )
        }

        // 3. Medical Record Notifications
        medicals.forEach { med ->
            val matchedPet = pets.find { it.petId == med.petId }
            val petName = matchedPet?.name ?: "Your Pet"
            list.add(
                AppNotification(
                    id = "med_${med.medicalRecordId}",
                    emoji = "💉",
                    title = "Medical Record: ${med.title}",
                    body = "$petName - ${med.recordType} at ${med.clinic.ifEmpty { "Vet Clinic" }}.",
                    tag = "🩺 Medical Alert",
                    timeText = med.date,
                    destinationId = R.id.medicalListFragment
                )
            )
        }

        // 4. Geotagged Location Notifications
        locations.forEach { loc ->
            val matchedPet = pets.find { it.petId == loc.petId }
            val petName = matchedPet?.name ?: "All Pets"
            list.add(
                AppNotification(
                    id = "loc_${loc.locationId}",
                    emoji = "📍",
                    title = "Location Saved: ${loc.name}",
                    body = "${loc.type} (${loc.address}) linked to $petName.",
                    tag = "📍 Geotagged Spot",
                    timeText = "Location",
                    destinationId = R.id.mapFragment
                )
            )
        }

        updateNotificationList(list)
    }

    private fun updateNotificationList(list: List<AppNotification>) {
        if (_binding == null) return
        if (list.isEmpty()) {
            binding.rvNotifications.visibility = View.GONE
            binding.layoutEmptyNotif.visibility = View.VISIBLE
        } else {
            binding.rvNotifications.visibility = View.VISIBLE
            binding.layoutEmptyNotif.visibility = View.GONE
            binding.rvNotifications.adapter = NotificationAdapter(list) { notif ->
                notif.destinationId?.let { dest ->
                    try {
                        findNavController().navigate(dest, notif.args)
                        dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class NotificationAdapter(
        private val items: List<AppNotification>,
        private val onItemClick: (AppNotification) -> Unit
    ) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemNotificationCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemNotificationCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.binding.tvNotifIconEmoji.text = item.emoji
            holder.binding.tvNotifTitle.text = item.title
            holder.binding.tvNotifBody.text = item.body
            holder.binding.tvNotifTag.text = item.tag
            holder.binding.tvNotifTime.text = item.timeText

            holder.itemView.setOnClickListener { onItemClick(item) }
        }

        override fun getItemCount(): Int = items.size
    }
}
