package com.example.petcare.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.databinding.FragmentNotificationListBinding
import com.example.petcare.databinding.ItemNotificationCardBinding
import com.example.petcare.ui.viewmodel.LocationViewModel
import com.example.petcare.ui.viewmodel.MedicalViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.example.petcare.ui.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar

data class NotificationItemData(
    val id: String,
    val emoji: String,
    val title: String,
    val body: String,
    val tag: String,
    val timeText: String,
    var isRead: Boolean = false,
    val destinationId: Int? = null,
    val args: Bundle? = null
)

class NotificationListFragment : Fragment() {

    private var _binding: FragmentNotificationListBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val medicalViewModel: MedicalViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    private var notificationList: MutableList<NotificationItemData> = mutableListOf()
    private var isAllRead: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackNotif.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnReadAllNotifications.setOnClickListener {
            isAllRead = true
            notificationList.forEach { it.isRead = true }
            renderNotifications()
            Snackbar.make(binding.root, "All notifications marked as read ✓", Snackbar.LENGTH_SHORT).show()
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
        val pets = petViewModel.pets.value ?: emptyList()
        val tasks = taskViewModel.allTasks.value ?: emptyList()
        val medicals = medicalViewModel.allMedicalRecords.value ?: emptyList()
        val locations = locationViewModel.allLocations.value ?: emptyList()

        val list = mutableListOf<NotificationItemData>()

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
                NotificationItemData(
                    id = "task_${task.taskId}",
                    emoji = emoji,
                    title = title,
                    body = body,
                    tag = tagText,
                    timeText = task.scheduledTime,
                    isRead = isAllRead,
                    destinationId = R.id.checklistFragment
                )
            )
        }

        // 2. New Pet Added Notifications
        pets.forEach { pet ->
            list.add(
                NotificationItemData(
                    id = "pet_${pet.petId}",
                    emoji = "🐾",
                    title = "New Pet Added: ${pet.name}! 🐾",
                    body = "${pet.name} (${pet.species} • ${pet.breed}) was successfully added to PetCare family.",
                    tag = "🐾 New Pet Enrolled",
                    timeText = "Active",
                    isRead = isAllRead,
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
                NotificationItemData(
                    id = "med_${med.medicalRecordId}",
                    emoji = "💉",
                    title = "Medical Record: ${med.title}",
                    body = "$petName - ${med.recordType} at ${med.clinic.ifEmpty { "Vet Clinic" }}.",
                    tag = "🩺 Medical Alert",
                    timeText = med.date,
                    isRead = isAllRead,
                    destinationId = R.id.medicalListFragment
                )
            )
        }

        // 4. Geotagged Location Notifications
        locations.forEach { loc ->
            val matchedPet = pets.find { it.petId == loc.petId }
            val petName = matchedPet?.name ?: "All Pets"
            list.add(
                NotificationItemData(
                    id = "loc_${loc.locationId}",
                    emoji = "📍",
                    title = "Location Saved: ${loc.name}",
                    body = "${loc.type} (${loc.address}) linked to $petName.",
                    tag = "📍 Geotagged Spot",
                    timeText = "Location",
                    isRead = isAllRead,
                    destinationId = R.id.mapFragment
                )
            )
        }

        notificationList = list
        renderNotifications()
    }

    private fun renderNotifications() {
        if (_binding == null) return
        if (notificationList.isEmpty()) {
            binding.rvNotifications.visibility = View.GONE
            binding.layoutEmptyNotif.visibility = View.VISIBLE
        } else {
            binding.rvNotifications.visibility = View.VISIBLE
            binding.layoutEmptyNotif.visibility = View.GONE
            binding.rvNotifications.adapter = FullNotificationAdapter(notificationList) { notif ->
                notif.isRead = true
                notif.destinationId?.let { dest ->
                    try {
                        findNavController().navigate(dest, notif.args)
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

    private class FullNotificationAdapter(
        private val items: List<NotificationItemData>,
        private val onItemClick: (NotificationItemData) -> Unit
    ) : RecyclerView.Adapter<FullNotificationAdapter.ViewHolder>() {

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

            if (item.isRead) {
                holder.itemView.alpha = 0.6f
            } else {
                holder.itemView.alpha = 1.0f
            }

            holder.itemView.setOnClickListener { onItemClick(item) }
        }

        override fun getItemCount(): Int = items.size
    }
}
