package com.android.profileapplication.ui.login

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
import com.android.profileapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val viewModel by viewModels<LoginViewModel>()
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setObservers()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiLoginState.collectLatest { loginState ->
                        when (loginState) {
                            is LoginState.ShowErrorSnackBar -> {
                                Snackbar.make(
                                    binding.root,
                                    loginState.message,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.onEvent(LoginUiEvent.OnSnackBarShown)
                            }

                            is LoginState.CloseSoftKeyboard -> {
                                closeKeyboard(binding.root)
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
    }

    // To close the soft keyboard, pass the current focused view to this function
    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}