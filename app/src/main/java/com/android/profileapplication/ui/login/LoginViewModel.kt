package com.android.profileapplication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.profileapplication.R
import com.android.profileapplication.utility.ResourceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val resourceHelper: ResourceHelper) : ViewModel() {

    private var _uiLoginState = MutableStateFlow<LoginState>(LoginState.DefaultState)
    val uiLoginState = _uiLoginState.asStateFlow()

    fun onEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            is LoginUiEvent.OnLoginClicked -> {
                viewModelScope.launch {
                    sendStateUpdateEvent(LoginState.CloseSoftKeyboard)
                    delay(10)
                    validateLogin(uiEvent.email, uiEvent.password)
                }
            }

            is LoginUiEvent.OnSnackBarShown -> {
                _uiLoginState.value = LoginState.DefaultState
            }

            else -> {
                // Handle other events if needed
            }
        }
    }

    private fun validateLogin(email: String, password: String) {

        //blank email
        if (email.isBlank() || email.trim().isBlank()) {
            sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    resourceHelper.getString(R.string.err_email_required)
                )
            )
            return
        }

        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        val isValidEmail = emailRegex.matches(email)

        if (!isValidEmail) {
            sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    resourceHelper.getString(R.string.err_invalid_email)
                )
            )
            return
        }

        if (email.length > 20) {
            sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    resourceHelper.getString(R.string.err_email_too_long)
                )
            )
            return
        }

        if (password.isBlank() || password.trim().isBlank()) {
            sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    resourceHelper.getString(R.string.err_password_is_required)
                )
            )
            return
        }

        if (password.length > 20) {
            sendStateUpdateEvent(
                LoginState.ShowErrorSnackBar(
                    resourceHelper.getString(R.string.err_password_too_long)
                )
            )
            return
        }

    }

    private fun sendStateUpdateEvent(event: LoginState) {
        viewModelScope.launch {
            _uiLoginState.emit(event)
        }
    }
}