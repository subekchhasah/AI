package com.example.petcare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.databinding.FragmentLoginBinding
import com.example.petcare.ui.viewmodel.AuthViewModel
import com.example.petcare.utils.ValidationUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pre-fill Emily credentials for university prototype demo ease
        binding.etEmail.setText("emily@example.com")
        binding.etPassword.setText("Password123")

        binding.btnSubmitLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            var isValid = true

            if (!ValidationUtils.isValidEmail(email)) {
                binding.tilEmail.error = getString(R.string.error_invalid_email)
                isValid = false
            } else {
                binding.tilEmail.error = null
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = getString(R.string.error_empty_field)
                isValid = false
            } else {
                binding.tilPassword.error = null
            }

            if (isValid) {
                authViewModel.login(email, password)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        authViewModel.authResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                Snackbar.make(
                    binding.root,
                    result.exceptionOrNull()?.message ?: getString(R.string.error_invalid_credentials),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        authViewModel.resetResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Snackbar.make(
                    binding.root,
                    result.getOrNull() ?: "Password reset successfully!",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    binding.root,
                    result.exceptionOrNull()?.message ?: "Password reset failed.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.tvGotoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun showForgotPasswordDialog() {
        val context = requireContext()
        val currentEmail = binding.etEmail.text.toString().trim()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 20)
        }

        val emailTil = TextInputLayout(context, null, com.google.android.material.R.style.Widget_Material3_TextInputLayout_OutlinedBox).apply {
            hint = "Account Email"
        }
        val etEmailReset = TextInputEditText(emailTil.context).apply {
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setText(currentEmail)
        }
        emailTil.addView(etEmailReset)

        val passTil = TextInputLayout(context, null, com.google.android.material.R.style.Widget_Material3_TextInputLayout_OutlinedBox).apply {
            hint = "New Password"
            endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            setPadding(0, 20, 0, 0)
        }
        val etNewPass = TextInputEditText(passTil.context).apply {
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        passTil.addView(etNewPass)

        layout.addView(emailTil)
        layout.addView(passTil)

        AlertDialog.Builder(context)
            .setTitle("Reset Password 🔑")
            .setMessage("Enter your registered email and choose a new password.")
            .setView(layout)
            .setPositiveButton("Reset Password") { _, _ ->
                val email = etEmailReset.text.toString().trim()
                val newPass = etNewPass.text.toString().trim()

                if (!ValidationUtils.isValidEmail(email)) {
                    Snackbar.make(binding.root, getString(R.string.error_invalid_email), Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (newPass.length < 6) {
                    Snackbar.make(binding.root, "Password must be at least 6 characters long.", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                authViewModel.resetPassword(email, newPass)
                binding.etEmail.setText(email)
                binding.etPassword.setText(newPass)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
