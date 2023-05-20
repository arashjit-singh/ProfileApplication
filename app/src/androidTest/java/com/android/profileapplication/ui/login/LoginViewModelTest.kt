package com.android.profileapplication.ui.login

import app.cash.turbine.test
import com.android.profileapplication.R
import com.android.profileapplication.utility.ResourceHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class LoginViewModelTest {

    lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var resourceHelper: ResourceHelper

    @Before
    fun setUp() {
        hiltRule.inject()
        loginViewModel = LoginViewModel(resourceHelper)
    }

    //test empty email and password


    @Test
    fun validateLogin_return_false_when_email_password_empty() = runTest {

        launch {
            loginViewModel.uiLoginState.test {
                assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
                assertEquals(
                    awaitItem(),
                    LoginState.ShowErrorSnackBar(resourceHelper.getString(R.string.err_email_required))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("", ""))
    }

    //test empty email and non empty password
    @Test
    fun validateLogin_return_false_when_no_email_address() = runTest {

        launch {
            loginViewModel.uiLoginState.test {
                assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
                assertEquals(
                    awaitItem(),
                    LoginState.ShowErrorSnackBar(resourceHelper.getString(R.string.err_email_required))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("", "password"))
    }

    //test invalid email id - invalid email format
    @Test
    fun validateLogin_return_false_invalide_email_id() = runTest {

        launch {
            loginViewModel.uiLoginState.test {
                assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
                assertEquals(
                    awaitItem(),
                    LoginState.ShowErrorSnackBar(resourceHelper.getString(R.string.err_invalid_email))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("abc", ""))

    }

    //  test email length
    @Test
    fun validateLogin_return_false_on_invalid_email_length() = runTest {

        launch {
            loginViewModel.uiLoginState.test {
                assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
                assertEquals(
                    awaitItem(),
                    LoginState.ShowErrorSnackBar(resourceHelper.getString(R.string.err_email_too_long))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        loginViewModel.onEvent(
            LoginUiEvent.OnLoginClicked(
                "abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc@gmail.com",
                ""
            )
        )

    }

    // test password empty and email non empty
    @Test
    fun validate_login_test_empty_password() = runTest {

        launch {
            loginViewModel.uiLoginState.test {
                assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
                assertEquals(
                    awaitItem(),
                    LoginState.ShowErrorSnackBar(resourceHelper.getString(R.string.err_password_is_required))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        loginViewModel.onEvent(LoginUiEvent.OnLoginClicked("test@gmail.com", ""))

    }

    //test password length
    @Test
    fun validate_login_test_invalid_password_length() = runTest {

        launch {
            loginViewModel.uiLoginState.test {
                assertEquals(awaitItem(), LoginState.CloseSoftKeyboard)
                assertEquals(
                    awaitItem(),
                    LoginState.ShowErrorSnackBar(resourceHelper.getString(R.string.err_password_too_long))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        loginViewModel.onEvent(
            LoginUiEvent.OnLoginClicked(
                "test@gmail.com",
                "abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc"
            )
        )

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