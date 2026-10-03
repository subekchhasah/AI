package com.example.petcare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.databinding.FragmentRegisterBinding
import com.example.petcare.ui.viewmodel.AuthViewModel
import com.example.petcare.utils.ValidationUtils
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitRegister.setOnClickListener {
            val name = binding.etRegName.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val password = binding.etRegPassword.text.toString().trim()
            val confirm = binding.etRegConfirm.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                binding.tilRegName.error = getString(R.string.error_empty_field)
                isValid = false
            } else {
                binding.tilRegName.error = null
            }

            if (!ValidationUtils.isValidEmail(email)) {
                binding.tilRegEmail.error = getString(R.string.error_invalid_email)
                isValid = false
            } else {
                binding.tilRegEmail.error = null
            }

            if (!ValidationUtils.isValidPassword(password)) {
                binding.tilRegPassword.error = getString(R.string.error_short_password)
                isValid = false
            } else {
                binding.tilRegPassword.error = null
            }

            if (password != confirm) {
                binding.tilRegConfirm.error = getString(R.string.error_password_mismatch)
                isValid = false
            } else {
                binding.tilRegConfirm.error = null
            }

            if (isValid) {
                authViewModel.register(name, email, password)
            }
        }

        authViewModel.authResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Snackbar.make(binding.root, getString(R.string.msg_registration_success), Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_register_to_home)
            } else {
                Snackbar.make(
                    binding.root,
                    result.exceptionOrNull()?.message ?: getString(R.string.error_duplicate_email),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.tvGotoLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
