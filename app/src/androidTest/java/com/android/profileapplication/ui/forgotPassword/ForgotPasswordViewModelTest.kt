package com.android.profileapplication.ui.forgotPassword

import app.cash.turbine.test
import com.android.profileapplication.R
import com.android.profileapplication.ui.login.LoginState
import com.android.profileapplication.ui.login.LoginUiEvent
import com.android.profileapplication.utility.ResourceHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ForgotPasswordViewModelTest {

    lateinit var viewModel: ForgotPasswordViewModel

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var resourceHelper: ResourceHelper

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = ForgotPasswordViewModel(resourceHelper)
    }

    //test empty email

    @Test
    fun test_empty_email() = runTest {

        launch {
            viewModel.uiForgotState.test {
                Assert.assertEquals(
                    awaitItem(),
                    ForgotPasswordUIState.OnShowSnackBar(resourceHelper.getString(R.string.err_email_required))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.onEvent(ForgotPasswordUIEvent.OnSubmitClick(""))
    }

    //test invalid email format

    @Test
    fun test_validateEmail_return_false_invalid_email_id() = runTest {

        launch {
            viewModel.uiForgotState.test {
                Assert.assertEquals(
                    awaitItem(),
                    ForgotPasswordUIState.OnShowSnackBar(resourceHelper.getString(R.string.err_invalid_email))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.onEvent(ForgotPasswordUIEvent.OnSubmitClick("abc"))

    }

    //test invalid email length
    @Test
    fun test_validateEmail_return_false_on_invalid_email_length() = runTest {

        launch {
            viewModel.uiForgotState.test {
                val event = awaitItem()
                Assert.assertEquals(
                    event,
                    ForgotPasswordUIState.OnShowSnackBar(resourceHelper.getString(R.string.err_email_too_long))
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.onEvent(ForgotPasswordUIEvent.OnSubmitClick("abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc@gmail.com"))

    }

}