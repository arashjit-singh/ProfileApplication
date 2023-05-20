package com.android.profileapplication.ui.login

sealed class LoginUiEvent {
    data class OnLoginClicked(val email: String, val password: String) : LoginUiEvent()
    object OnForgotPassword : LoginUiEvent()
    object OnLoginWithGoogleClicked : LoginUiEvent()
    object OnRegisterClicked : LoginUiEvent()
    object OnSnackBarShown : LoginUiEvent()
}
