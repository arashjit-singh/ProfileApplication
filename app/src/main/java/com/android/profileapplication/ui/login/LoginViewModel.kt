package com.android.profileapplication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.profileapplication.R
import com.android.profileapplication.data.remote.repository.FirebaseAuthRepository
import com.android.profileapplication.utility.Constants
import com.android.profileapplication.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository) :
    ViewModel() {

    private var _uiLoginState = MutableSharedFlow<LoginState>()
    val uiLoginState = _uiLoginState.asSharedFlow()

    fun onEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            is LoginUiEvent.OnLoginClicked -> {
                sendStateUpdateEvent(LoginState.CloseSoftKeyboard)
                validateLogin(uiEvent.email, uiEvent.password)
            }

            is LoginUiEvent.OnSnackBarShown -> {
                sendStateUpdateEvent(LoginState.DefaultState)
            }

            is LoginUiEvent.OnForgotPassword -> {
                sendStateUpdateEvent(LoginState.OnNavigate(Constants.ROUTE_FORGOT_PASSWORD))
            }

            else -> {
                // Handle other events if needed
            }
        }
    }

    private fun validateLogin(email: String, password: String) {

        //blank email or password
        if (email.isBlank() || email.trim().isBlank() || password.isBlank() || password.trim()
                .isBlank()
        ) {
            return sendStateUpdateEvent(
                LoginState.ShowSnackBarFromResId(
                    R.string.email_password_required
                )
            )
        }

        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        val isValidEmail = emailRegex.matches(email)

        if (!isValidEmail) {
            return sendStateUpdateEvent(
                LoginState.ShowSnackBarFromResId(
                    R.string.err_invalid_email
                )
            )

        }

        if (email.length > /*R.integer.email_length*/ 20) {
            return sendStateUpdateEvent(
                LoginState.ShowSnackBarFromResId(
                    (R.string.err_email_too_long)
                )
            )
        }

        if (password.length > 20) {
            return sendStateUpdateEvent(
                LoginState.ShowSnackBarFromResId(
                    (R.string.err_password_too_long)
                )
            )
        }

        signInUserWithFirebase(email, password)

    }

    private fun signInUserWithFirebase(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signInUser(email, password).collect {
                when (it) {
                    is Resource.Error -> {
                        sendStateUpdateEvent(LoginState.OnLoading(false))
                        sendStateUpdateEvent(LoginState.ShowSnackBar(it.message ?: ""))
                    }

                    is Resource.Loading -> {
                        sendStateUpdateEvent(LoginState.OnLoading(true))
                    }

                    is Resource.Success -> {
                        sendStateUpdateEvent(LoginState.OnLoading(false))
                        sendStateUpdateEvent(LoginState.ShowSnackBarFromResId(R.string.signIn_successfully))
                    }
                }
            }
        }
    }

    private fun sendStateUpdateEvent(event: LoginState) {
        viewModelScope.launch {
            _uiLoginState.emit(event)
        }
    }
}