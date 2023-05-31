package com.android.profileapplication.ui.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.profileapplication.R
import com.android.profileapplication.data.remote.repository.FirebaseAuthRepository
import com.android.profileapplication.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(val authRepository: FirebaseAuthRepository) :
    ViewModel() {

    private var _uiForgotPwdState =
        MutableStateFlow<ForgotPasswordUIState>(ForgotPasswordUIState.EmptyState)
    val uiForgotState = _uiForgotPwdState.asStateFlow()

    fun onEvent(event: ForgotPasswordUIEvent) {
        when (event) {
            is ForgotPasswordUIEvent.OnSubmitClick -> {
                sendEventToUI(ForgotPasswordUIState.CloseSoftKeyboard)
                validateEmail(event.emailId)
            }

            ForgotPasswordUIEvent.ResetState -> {
                _uiForgotPwdState.value = ForgotPasswordUIState.EmptyState
            }
        }
    }

    private fun validateEmail(emailId: String) {
        if (emailId.isBlank() || emailId.trim().isBlank()) return sendEventToUI(
            ForgotPasswordUIState.OnShowSnackBar(R.string.err_email_required)
        )

        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        val isValidEmail = emailRegex.matches(emailId)

        if (!isValidEmail) {
            return sendEventToUI(
                ForgotPasswordUIState.OnShowSnackBar(
                    R.string.err_invalid_email
                )
            )
        }

        if (emailId.length > 20) {
            return sendEventToUI(
                ForgotPasswordUIState.OnShowSnackBar(
                    R.string.err_email_too_long
                )
            )
        }

        resetPassword(emailId)

    }

    private fun resetPassword(emailId: String) {
        viewModelScope.launch {
            authRepository.resetPassword(emailId).collect {
                when (it) {
                    is Resource.Error -> {
                        sendEventToUI(ForgotPasswordUIState.Loading(false))
                        delay(100)
                        sendEventToUI(ForgotPasswordUIState.ShowSnackbar(it.message ?: ""))
                    }

                    is Resource.Success -> {
                        sendEventToUI(ForgotPasswordUIState.Loading(false))
                        delay(100)
                        sendEventToUI(ForgotPasswordUIState.ShowSnackbar(it.data ?: ""))
                    }

                    is Resource.Loading -> {
                        sendEventToUI(ForgotPasswordUIState.Loading(true))
                    }

                }
            }
        }
    }

    private fun sendEventToUI(event: ForgotPasswordUIState) {
        viewModelScope.launch {
            _uiForgotPwdState.emit(event)
        }
    }

}