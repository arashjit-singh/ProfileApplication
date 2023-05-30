package com.android.profileapplication.ui.forgotPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.profileapplication.R
import com.android.profileapplication.utility.ResourceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val resourceHelper: ResourceHelper) :
    ViewModel() {

    private var _uiForgotPwdState =
        MutableStateFlow<ForgotPasswordUIState>(ForgotPasswordUIState.InitialState)
    val uiForgotState = _uiForgotPwdState.asStateFlow()

    fun onEvent(event: ForgotPasswordUIEvent) {
        when (event) {
            is ForgotPasswordUIEvent.OnSubmitClick -> {
                validateEmail(event.emailId)
            }

            ForgotPasswordUIEvent.SnackBarShown -> {
                Log.i("TAG","TAG")
            }
        }
    }

    private fun validateEmail(emailId: String) {
        if (emailId.isBlank() || emailId.trim().isBlank())
            return sendEventToUI(ForgotPasswordUIState.OnShowSnackBar(resourceHelper.getString(R.string.err_email_required)))

        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        val isValidEmail = emailRegex.matches(emailId)

        if (!isValidEmail) {
            return sendEventToUI(
                ForgotPasswordUIState.OnShowSnackBar(
                    resourceHelper.getString(R.string.err_invalid_email)
                )
            )
        }

        if (emailId.length > resourceHelper.getInt(R.integer.email_length)) {
            return sendEventToUI(
                ForgotPasswordUIState.OnShowSnackBar(
                    resourceHelper.getString(R.string.err_email_too_long)
                )
            )
        }

    }

    private fun sendEventToUI(event: ForgotPasswordUIState) {
        viewModelScope.launch {
            _uiForgotPwdState.emit(event)
        }
    }

}