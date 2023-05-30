package com.android.profileapplication.ui.forgotPassword

sealed class ForgotPasswordUIEvent {
    data class OnSubmitClick(val emailId: String) : ForgotPasswordUIEvent()
    object SnackBarShown : ForgotPasswordUIEvent()
}
