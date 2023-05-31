package com.android.profileapplication.ui.login

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.android.profileapplication.R
import com.android.profileapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setObservers()
        setTextSpan()
    }

    private fun setTextSpan() {

        val fullText = getString(R.string.new_to_logistics_register)
        val coloredText = "Register"

        val spannable = SpannableStringBuilder(fullText)
        val startIndex = fullText.indexOf(coloredText)
        val endIndex = startIndex + coloredText.length

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorPrimary)),
            startIndex, // start
            endIndex, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            StyleSpan(Typeface.BOLD), startIndex, // start
            endIndex, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            RelativeSizeSpan(1.1f), startIndex, // start
            endIndex, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.registerTxtVw.text = spannable

    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiLoginState.collectLatest { loginState ->
                        when (loginState) {
                            is LoginState.ShowSnackBarFromResId -> {
                                val message = getString(loginState.message)
                                Snackbar.make(
                                    binding.root, message, Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.onEvent(LoginUiEvent.OnSnackBarShown)
                            }

                            is LoginState.ShowSnackBar -> {
                                Snackbar.make(
                                    binding.root, loginState.message, Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.onEvent(LoginUiEvent.OnSnackBarShown)
                            }

                            is LoginState.CloseSoftKeyboard -> {
                                closeKeyboard(binding.root)
                            }

                            is LoginState.OnNavigate -> {
                                findNavController().navigate(R.id.forgotPasswordFragment)
                            }

                            is LoginState.OnLoading -> {
                                binding.progressBar.visibility =
                                    if (loginState.isLoading) View.VISIBLE else View.GONE
                            }

                            else -> {
                                Log.i("Event", "Event")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.loginBtn.setOnClickListener {
            viewModel.onEvent(
                LoginUiEvent.OnLoginClicked(
                    binding.emailAddressEdtTxt.text.toString(),
                    binding.passwordAddressEdtTxt.text.toString()
                )
            )
        }
        binding.forgotPwdTxtVw.setOnClickListener {
            viewModel.onEvent(LoginUiEvent.OnForgotPassword)
        }
    }

    // To close the soft keyboard, pass the current focused view to this function
    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}