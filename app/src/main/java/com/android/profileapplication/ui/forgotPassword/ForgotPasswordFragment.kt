package com.android.profileapplication.ui.forgotPassword

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.profileapplication.databinding.FragmentForgotPasswordBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private val viewModel by viewModels<ForgotPasswordViewModel>()
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiForgotState.collect {
                    when (it) {

                        is ForgotPasswordUIState.OnNavigate -> {
                            Log.i("TAG", "TODO")
                        }

                        is ForgotPasswordUIState.OnShowSnackBar -> {
                            val message = getString(it.messageId)
                            Snackbar.make(
                                binding.root,
                                message,
                                Snackbar.LENGTH_SHORT
                            ).show()
                            viewModel.onEvent(ForgotPasswordUIEvent.ResetState)
                        }

                        is ForgotPasswordUIState.ShowSnackbar -> {
                            Snackbar.make(
                                binding.root,
                                it.message,
                                Snackbar.LENGTH_SHORT
                            ).show()
                            viewModel.onEvent(ForgotPasswordUIEvent.ResetState)
                        }

                        is ForgotPasswordUIState.Loading -> {
                            binding.progressBar.visibility =
                                if (it.isLoading) View.VISIBLE else View.GONE
                            viewModel.onEvent(ForgotPasswordUIEvent.ResetState)
                        }

                        is ForgotPasswordUIState.CloseSoftKeyboard -> {
                            closeKeyboard(binding.root)
                        }

                        else -> {
                            Log.i("TAG", "Else branch")
                        }

                    }
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.submitBtn.setOnClickListener {
            viewModel.onEvent(ForgotPasswordUIEvent.OnSubmitClick(binding.emailAddressEdtTxtFgtPwd.text.toString()))
        }
    }

    // To close the soft keyboard, pass the current focused view to this function
    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}