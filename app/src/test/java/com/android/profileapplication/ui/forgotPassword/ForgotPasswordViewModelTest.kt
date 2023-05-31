package com.android.profileapplication.ui.forgotPassword

import app.cash.turbine.test
import com.android.profileapplication.MainDispatcherRule
import com.android.profileapplication.R
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForgotPasswordViewModelTest {

    lateinit var viewModel: ForgotPasswordViewModel

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = ForgotPasswordViewModel()
    }

    //test empty email
    @Test
    fun test_empty_email() = runTest {
        viewModel.uiForgotState.test {
            viewModel.onEvent(ForgotPasswordUIEvent.OnSubmitClick(""))
            assertEquals(awaitItem(), ForgotPasswordUIState.EmptyState)
            assertEquals(
                awaitItem(), ForgotPasswordUIState.OnShowSnackBar(R.string.err_email_required)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    //test invalid email format
    @Test
    fun test_validateEmail_return_false_invalid_email_id() = runTest {
        viewModel.uiForgotState.test {
            viewModel.onEvent(ForgotPasswordUIEvent.OnSubmitClick("abc"))
            assertEquals(awaitItem(), ForgotPasswordUIState.EmptyState)
            assertEquals(
                awaitItem(), ForgotPasswordUIState.OnShowSnackBar(R.string.err_invalid_email)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    //test invalid email length
    @Test
    fun test_validateEmail_return_false_on_invalid_email_length() = runTest {
        viewModel.uiForgotState.test {
            viewModel.onEvent(ForgotPasswordUIEvent.OnSubmitClick("abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc@gmail.com"))
            assertEquals(awaitItem(), ForgotPasswordUIState.EmptyState)
            assertEquals(
                awaitItem(), ForgotPasswordUIState.OnShowSnackBar(R.string.err_email_too_long)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }


}