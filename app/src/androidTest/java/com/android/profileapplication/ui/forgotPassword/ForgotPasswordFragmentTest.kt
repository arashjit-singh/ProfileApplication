package com.android.profileapplication.ui.forgotPassword

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.profileapplication.ui.MainActivity
import com.android.profileapplication.R
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgotPasswordFragmentTest {


    @get:Rule
    val activityScenario: ActivityScenario<MainActivity> =
        ActivityScenario.launch(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun test_snackbar_shown_on_empty_email() {
        onView(withId(R.id.submitBtn)).perform(click())
        onView(withText("Email is Required")).check(matches(isDisplayed()))
    }
}