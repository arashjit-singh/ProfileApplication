package com.android.profileapplication.ui.login

sealed class LoginState {
    data class ShowSnackBarFromResId(val message: Int) : LoginState()
    data class ShowSnackBar(val message: String) : LoginState()
    data class OnNavigate(val route: String) : LoginState()
    data class OnLoading(val isLoading: Boolean) : LoginState()
    object DefaultState : LoginState()
    object CloseSoftKeyboard : LoginState()
}
