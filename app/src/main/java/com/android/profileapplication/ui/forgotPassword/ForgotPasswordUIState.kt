package com.android.profileapplication.ui.forgotPassword

sealed class ForgotPasswordUIState {
    data class OnShowSnackBar(val messageId: Int) : ForgotPasswordUIState()
    data class ShowSnackbar(val message: String) : ForgotPasswordUIState()
    object EmptyState : ForgotPasswordUIState()
    data class OnNavigate(val route: String) : ForgotPasswordUIState()
    data class Loading(val isLoading: Boolean) : ForgotPasswordUIState()

    object CloseSoftKeyboard : ForgotPasswordUIState()
}
