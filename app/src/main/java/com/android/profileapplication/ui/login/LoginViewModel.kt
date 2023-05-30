package com.android.profileapplication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.profileapplication.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

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
//                _uiLoginState.value = LoginState.DefaultState
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
                LoginState.ShowErrorSnackBar(
                    R.string.email_password_required
                )
            )
        }

        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        val isValidEmail = emailRegex.matches(email)

        if (!isValidEmail) {
            return sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    R.string.err_invalid_email
                )
            )

        }

        if (email.length > /*R.integer.email_length*/ 20) {
            return sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    (R.string.err_email_too_long)
                )
            )
        }

        if (password.length > 20) {
            return sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    (R.string.err_password_too_long)
                )
            )
        }

    }

    private fun sendStateUpdateEvent(event: LoginState) {
        viewModelScope.launch {
            _uiLoginState.emit(event)
        }
    }
}