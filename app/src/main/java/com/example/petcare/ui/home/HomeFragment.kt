package com.example.petcare.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.data.local.entities.CareTask
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentHomeBinding
import com.example.petcare.databinding.ItemPetCardHomeBinding
import com.example.petcare.databinding.ItemTaskCardBinding
import com.example.petcare.ui.viewmodel.PetViewModel
import com.example.petcare.ui.viewmodel.TaskViewModel
import com.example.petcare.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()

    private var completedTasks = 0
    private var outstandingTasks = 0
    private var petsList: List<Pet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.getUserName()
        val userInitial = userName.trim().firstOrNull()?.toString()?.uppercase() ?: "S"
        binding.tvUserInitial.text = userInitial

        val greeting = getDynamicGreeting()
        binding.tvWelcomeTitle.text = "$greeting, $userName!"

        val formattedDate = SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault()).format(Date())
        binding.tvCurrentDate.text = formattedDate

        // Task Summary Observer & Dynamic Progress Calculation
        taskViewModel.completedCount.observe(viewLifecycleOwner) { completed ->
            completedTasks = completed ?: 0
            binding.tvCompletedCount.text = "$completedTasks Tasks"
            updateCareProgress()
        }

        taskViewModel.outstandingCount.observe(viewLifecycleOwner) { outstanding ->
            outstandingTasks = outstanding ?: 0
            binding.tvOutstandingCount.text = "$outstandingTasks Tasks"
            updateCareProgress()
        }

        // Quick Action Card Listeners
        val navToAddPet = View.OnClickListener { findNavController().navigate(R.id.action_home_to_addPet) }
        val navToChecklist = View.OnClickListener { findNavController().navigate(R.id.action_home_to_checklist) }
        val navToAddTask = View.OnClickListener { findNavController().navigate(R.id.action_home_to_addTask) }
        val navToLocations = View.OnClickListener { findNavController().navigate(R.id.action_home_to_locations) }

        binding.cardActionAddPet.setOnClickListener(navToAddPet)
        binding.cardActionChecklist.setOnClickListener(navToChecklist)
        binding.cardActionAddTask.setOnClickListener(navToAddTask)
        binding.cardActionLocations.setOnClickListener(navToLocations)

        // See All Pets action
        binding.btnSeeAllPets.setOnClickListener {
            findNavController().navigate(R.id.petListFragment)
        }

        // Quick Insights Listeners
        binding.cardInsightMedical.setOnClickListener {
            findNavController().navigate(R.id.medicalListFragment)
        }
        
        binding.cardInsightExpenses.setOnClickListener {
            findNavController().navigate(R.id.expenseListFragment)
        }

        // Top Header Icons - Open Full-Page Notification List
        binding.btnNotifications.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notifications)
        }
        
        binding.btnUserProfile.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        // See All Tasks action
        binding.btnSeeAllTasks.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_checklist)
        }

        petViewModel.pets.observe(viewLifecycleOwner) { petList ->
            petsList = petList ?: emptyList()
            binding.rvHomePets.adapter = HomePetAdapter(petsList) { pet ->
                val bundle = Bundle().apply { putLong("petId", pet.petId) }
                findNavController().navigate(R.id.action_home_to_petDetail, bundle)
            }
            refreshHomeTasks()
        }

        // Today's Care Tasks List Observer
        taskViewModel.allTasks.observe(viewLifecycleOwner) { _ ->
            refreshHomeTasks()
        }
    }

    private fun refreshHomeTasks() {
        val tasks = taskViewModel.allTasks.value ?: emptyList()
        if (tasks.isEmpty()) {
            binding.layoutHomeEmptyTasks.visibility = View.VISIBLE
            binding.rvHomeTasks.visibility = View.GONE
        } else {
            binding.layoutHomeEmptyTasks.visibility = View.GONE
            binding.rvHomeTasks.visibility = View.VISIBLE
            binding.rvHomeTasks.adapter = HomeTaskAdapter(
                tasks = tasks,
                pets = petsList,
                onToggleComplete = { task -> taskViewModel.toggleTaskCompletion(task) },
                onDeleteClick = { task -> taskViewModel.deleteTask(task) {} }
            )
        }
    }

    private fun updateCareProgress() {
        val total = completedTasks + outstandingTasks
        val percentage = if (total > 0) ((completedTasks.toDouble() / total) * 100).toInt() else 0
        binding.progressCareTasks.progress = percentage
        binding.tvCarePercentage.text = "$percentage% Complete"

        binding.tvMotivationalText.text = when {
            total == 0 -> "🐾 Add your first care task to start tracking!"
            percentage == 100 -> "🎉 Amazing! All care tasks complete for today!"
            percentage >= 50 -> "🐾 Great progress! Your pets are feeling happy & healthy."
            else -> "⏰ Don't forget your scheduled pet care tasks today!"
        }
    }

    private fun getDynamicGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..21 -> "Good Evening"
            else -> "Welcome back"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class HomePetAdapter(
        private val pets: List<Pet>,
        private val onItemClick: (Pet) -> Unit
    ) : RecyclerView.Adapter<HomePetAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemPetCardHomeBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemPetCardHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val pet = pets[position]
            holder.binding.tvPetName.text = pet.name
            holder.binding.tvPetBreed.text = pet.breed
            holder.binding.tvPetInitial.text = pet.name.trim().firstOrNull()?.toString()?.uppercase() ?: "P"

            pet.imageUri?.let { uriStr ->
                com.example.petcare.utils.ImageLoaderUtils.loadPetImage(holder.binding.ivPetThumb, uriStr)
            }

            holder.itemView.setOnClickListener { onItemClick(pet) }
        }

        override fun getItemCount(): Int = pets.size
    }

    private class HomeTaskAdapter(
        private val tasks: List<CareTask>,
        private val pets: List<Pet>,
        private val onToggleComplete: (CareTask) -> Unit,
        private val onDeleteClick: (CareTask) -> Unit
    ) : RecyclerView.Adapter<HomeTaskAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemTaskCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemTaskCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val task = tasks[position]
            holder.binding.tvTaskTitle.text = task.title

            // Match Pet Name explicitly
            val matchedPet = pets.find { it.petId == task.petId }
            val petLabel = if (matchedPet != null) "${matchedPet.name} (${matchedPet.species})" else "All Pets"
            holder.binding.tvTaskPetBadge.text = "🐾 Pet: $petLabel"

            val formattedTime = try {
                val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
                val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val date = sdf24.parse(task.scheduledTime)
                if (date != null) sdf12.format(date) else task.scheduledTime
            } catch (e: Exception) {
                task.scheduledTime
            }
            holder.binding.tvTaskTimeCategory.text = "${task.scheduledDate} • $formattedTime • ${task.category}"
            holder.binding.tvTaskPriorityBadge.text = task.priority.uppercase()
            
            holder.binding.cbTaskComplete.isChecked = task.isCompleted

            if (task.isCompleted) {
                holder.binding.tvTaskTitle.paintFlags =
                    holder.binding.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.binding.tvTaskTitle.paintFlags =
                    holder.binding.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            holder.binding.cbTaskComplete.setOnClickListener { onToggleComplete(task) }
            holder.binding.btnTaskDelete.setOnClickListener { onDeleteClick(task) }
        }

        override fun getItemCount(): Int = tasks.size
    }
}
