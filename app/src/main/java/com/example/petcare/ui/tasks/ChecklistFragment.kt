package com.example.petcare.ui.tasks

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.data.local.entities.CareTask
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentChecklistBinding
import com.example.petcare.databinding.ItemTaskCardBinding
import com.example.petcare.ui.viewmodel.PetViewModel
import com.example.petcare.ui.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar

class ChecklistFragment : Fragment() {

    private var _binding: FragmentChecklistBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var petsList: List<Pet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_checklist_to_addTask)
        }

        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petsList = pets ?: emptyList()
            refreshTasksList()
        }

        taskViewModel.allTasks.observe(viewLifecycleOwner) { _ ->
            refreshTasksList()
        }
    }

    private fun refreshTasksList() {
        val tasks = taskViewModel.allTasks.value ?: emptyList()
        if (tasks.isEmpty()) {
            binding.layoutEmptyTasks.visibility = View.VISIBLE
            binding.rvTasks.visibility = View.GONE
        } else {
            binding.layoutEmptyTasks.visibility = View.GONE
            binding.rvTasks.visibility = View.VISIBLE
            binding.rvTasks.adapter = TaskAdapter(
                tasks = tasks,
                pets = petsList,
                onToggleComplete = { task -> taskViewModel.toggleTaskCompletion(task) },
                onEditClick = { task ->
                    val bundle = bundleOf("taskId" to task.taskId, "petId" to (task.petId ?: -1L))
                    findNavController().navigate(R.id.action_checklist_to_editTask, bundle)
                },
                onDeleteClick = { task -> showDeleteDialog(task) },
                onDelegateClick = { task ->
                    val bundle = bundleOf("petId" to (task.petId ?: 1L))
                    findNavController().navigate(R.id.action_checklist_to_smsDelegation, bundle)
                }
            )
        }
    }

    private fun showDeleteDialog(task: CareTask) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_delete_task_title)
            .setMessage(R.string.dialog_delete_task_msg)
            .setPositiveButton(R.string.btn_delete) { _, _ ->
                taskViewModel.deleteTask(task) {
                    Snackbar.make(binding.root, getString(R.string.msg_task_deleted), Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class TaskAdapter(
        private val tasks: List<CareTask>,
        private val pets: List<Pet>,
        private val onToggleComplete: (CareTask) -> Unit,
        private val onEditClick: (CareTask) -> Unit,
        private val onDeleteClick: (CareTask) -> Unit,
        private val onDelegateClick: (CareTask) -> Unit
    ) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

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
                val sdf24 = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                val sdf12 = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
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
            holder.binding.btnTaskEdit.setOnClickListener { onEditClick(task) }
            holder.binding.btnTaskDelete.setOnClickListener { onDeleteClick(task) }
            holder.binding.btnTaskDelegate.setOnClickListener { onDelegateClick(task) }
        }

        override fun getItemCount(): Int = tasks.size
    }
}
