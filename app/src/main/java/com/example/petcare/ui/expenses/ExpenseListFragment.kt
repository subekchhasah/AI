package com.example.petcare.ui.expenses

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
import com.example.petcare.data.local.entities.Expense
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.databinding.FragmentExpenseListBinding
import com.example.petcare.databinding.ItemExpenseCardBinding
import com.example.petcare.ui.viewmodel.ExpenseViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private val expenseViewModel: ExpenseViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private var petsList: List<Pet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddExpense.setOnClickListener {
            findNavController().navigate(R.id.action_expenseList_to_addExpense)
        }

        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petsList = pets ?: emptyList()
            refreshExpenseList()
        }

        expenseViewModel.totalExpenseAmount.observe(viewLifecycleOwner) { total ->
            val amount = total ?: 0.0
            binding.tvTotalExpenseText.text = "Total Expenses: £${String.format(Locale.getDefault(), "%.2f", amount)} 💰"
        }

        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { _ ->
            refreshExpenseList()
        }
    }

    private fun refreshExpenseList() {
        val expenses = expenseViewModel.allExpenses.value ?: emptyList()
        
        // Calculate Per-Pet Spending Breakdown
        val breakdownBuilder = StringBuilder()
        if (petsList.isNotEmpty()) {
            petsList.forEach { pet ->
                val petSum = expenses.filter { it.petId == pet.petId }.sumOf { it.amount }
                breakdownBuilder.append("🐾 ${pet.name} (${pet.species}): £${String.format(Locale.getDefault(), "%.2f", petSum)}\n")
            }
            val generalSum = expenses.filter { it.petId == null }.sumOf { it.amount }
            if (generalSum > 0.0) {
                breakdownBuilder.append("🐾 General / Other: £${String.format(Locale.getDefault(), "%.2f", generalSum)}")
            }
        } else {
            breakdownBuilder.append("Add pets to see individual spending breakdown.")
        }
        binding.tvPetExpenseBreakdown.text = breakdownBuilder.toString().trim()

        // Bind Adapter
        binding.rvExpenses.adapter = ExpenseAdapter(
            expenses = expenses,
            pets = petsList,
            onItemClick = { expense ->
                val bundle = bundleOf("expenseId" to expense.expenseId)
                findNavController().navigate(R.id.addEditExpenseFragment, bundle)
            },
            onDeleteClick = { expense ->
                showDeleteDialog(expense)
            }
        )
    }

    private fun showDeleteDialog(expense: Expense) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete '${expense.description}' (£${String.format(Locale.getDefault(), "%.2f", expense.amount)})?")
            .setPositiveButton("Delete") { _, _ ->
                expenseViewModel.deleteExpense(expense) {
                    Snackbar.make(binding.root, "Expense deleted", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class ExpenseAdapter(
        private val expenses: List<Expense>,
        private val pets: List<Pet>,
        private val onItemClick: (Expense) -> Unit,
        private val onDeleteClick: (Expense) -> Unit
    ) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemExpenseCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemExpenseCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val expense = expenses[position]
            holder.binding.tvExpenseDesc.text = expense.description
            
            // Match Pet Name explicitly
            val matchedPet = pets.find { it.petId == expense.petId }
            val petLabel = if (matchedPet != null) "${matchedPet.name} (${matchedPet.species})" else "General / All Pets"
            holder.binding.tvExpensePetBadge.text = "🐾 For: $petLabel"

            holder.binding.tvExpenseCategoryDate.text =
                holder.itemView.context.getString(R.string.expense_category_date_format, expense.category, expense.date)
            holder.binding.tvExpenseAmount.text =
                holder.itemView.context.getString(R.string.expense_amount_format, expense.amount)

            holder.itemView.setOnClickListener { onItemClick(expense) }
            holder.binding.btnExpenseDelete.setOnClickListener { onDeleteClick(expense) }
        }

        override fun getItemCount(): Int = expenses.size
    }
}
