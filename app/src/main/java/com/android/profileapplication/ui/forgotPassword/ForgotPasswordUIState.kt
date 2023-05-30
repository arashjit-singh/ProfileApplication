package com.android.profileapplication.ui.forgotPassword

sealed class ForgotPasswordUIState {
    data class OnShowSnackBar(val message: String) : ForgotPasswordUIState()
    object InitialState : ForgotPasswordUIState()
    data class OnNavigate(val route: String) : ForgotPasswordUIState()
}
