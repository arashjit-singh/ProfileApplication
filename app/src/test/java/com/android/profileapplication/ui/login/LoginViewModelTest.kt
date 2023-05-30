package com.android.profileapplication.ui.login

import app.cash.turbine.test
import com.android.profileapplication.MainDispatcherRule
import com.android.profileapplication.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val instantRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel()
    }

    //test empty email and password
    @Test
    fun validateLogin_return_false_when_email_password_empty() = runTest {
        loginViewModel.uiLoginState.test {
            loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("", ""))
            assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
            assertEquals(
                awaitItem(), LoginState.ShowErrorSnackBar(R.string.email_password_required)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    //test empty email and non empty password
    @Test
    fun validateLogin_return_false_when_no_email_address() = runTest {
        loginViewModel.uiLoginState.test {
            loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("", "password"))
            assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
            assertEquals(
                awaitItem(), LoginState.ShowErrorSnackBar(R.string.email_password_required)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    //test invalid email id - invalid email format
    @Test
    fun validateLogin_return_false_invalid_email_id() = runTest {
        loginViewModel.uiLoginState.test {
            loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("abc", "password"))
            assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
            assertEquals(
                awaitItem(), LoginState.ShowErrorSnackBar(R.string.err_invalid_email)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    //  test email length
    @Test
    fun validateLogin_return_false_on_invalid_email_length() = runTest {

        loginViewModel.uiLoginState.test {
            loginViewModel.onEvent(
                LoginUiEvent.OnLoginClicked(
                    "abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc@gmail.com",
                    "password"
                )
            )
            assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
            assertEquals(
                awaitItem(), LoginState.ShowErrorSnackBar(R.string.err_email_too_long)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    // test password empty and email non empty
    @Test
    fun validate_login_test_empty_password() = runTest {

        launch {
            loginViewModel.uiLoginState.test {
                loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("test@gmail.com", ""))
                assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
                assertEquals(
                    awaitItem(), LoginState.ShowErrorSnackBar(R.string.email_password_required)
                )
                cancelAndIgnoreRemainingEvents()
            }
        }


    }

    //test password length
    @Test
    fun validate_login_test_invalid_password_length() = runTest {

        loginViewModel.uiLoginState.test {
            loginViewModel.onEvent(
                LoginUiEvent.OnLoginClicked(
                    "test@gmail.com",
                    "abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc"
                )
            )
            assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
            assertEquals(
                awaitItem(), LoginState.ShowErrorSnackBar(R.string.err_password_too_long)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    //  Valid email and password return true

    /*@Test
    fun test_valid_email_password_return_true() = runTest {

        launch {
            loginViewModel.UiLoginState.test {
                assertEquals(awaitItem(), LoginState.ShowErrorSnackBar("Email is required"))
                cancelAndIgnoreRemainingEvents()
            }
        }


        loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("test@gmail.com", "password"))
    }*/


}