package com.abnamro.apps.referenceandroid

import android.support.design.widget.Snackbar.*
import android.support.test.espresso.*
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingResource.ResourceCallback
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.abnamro.apps.referenceandroid.WaitActions.waitUntilDismiss
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UserExploringAppFunctionalityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    /*
    Customer Journey:

    After launching app the customer performs:
    - Read title
    - Read message
    - Open overflow menu to view options
    - Select option and confirm option dismisses
    - Tap button and read message in time frame
     */

    @Test
    fun iWantToReadEverythingAndTapEverywhere() {

        // Read title
        onView(toolbarText("ReferenceAndroid")).check(matches(isCompletelyDisplayed()))

        // Read message
        onView(fragmentText("Hello World!")).check(matches(isCompletelyDisplayed()))

        // Open overflow menu
        openContextualActionModeOverflowMenu()

        // Read menu option
        onView(overflowText("Settings")).check(matches(isCompletelyDisplayed()))

        // Select menu option to see it disappear
        onView(overflowText("Settings")).perform(click()).check(doesNotExist())

        // Tap red button
        onView(FLOATING_ACTION_BUTTON).check(matches(isCompletelyDisplayed())).perform(click())

        // See Snackbar appear
        onView(SNACKBAR).check(matches(isCompletelyDisplayed()))

        // Read Snackbar message
        onView(snackbarText("Replace with your own action")).check(matches(isCompletelyDisplayed()))

        // Wait for Snackbar to disappear
        onView(SNACKBAR).perform(waitUntilDismiss())

        // Check Snackbar has gone - back to initial state
        onView(SNACKBAR).check(doesNotExist())
    }

    companion object {
        private val FLOATING_ACTION_BUTTON = withId(R.id.fab)
        private val SNACKBAR = isAssignableFrom(SnackbarLayout::class.java)
        private fun toolbarText(text: String) = allOf(withParent(withId(R.id.toolbar)), withText(text))
        private fun fragmentText(text: String) = allOf(withParent(withId(R.id.fragment)), withText(text))
        private fun overflowText(text: String) = allOf(withId(R.id.title), withText(text))
        private fun snackbarText(text: String) = allOf(withId(android.support.design.R.id.snackbar_text), withText(text))
    }
}

// Solution adapted from author of https://stackoverflow.com/questions/59689109/how-to-wait-for-async-task-in-espresso-without-idlingresource
object WaitActions {
    fun waitUntilDismiss(): ViewAction = object : ViewAction {

        private val callback = ViewDismissCallback()

        override fun getConstraints(): Matcher<View> {
            return allOf(
                withEffectiveVisibility(Visibility.VISIBLE),
                isAssignableFrom(View::class.java)
            )
        }

        override fun getDescription(): String = "wait for view dimiss"

        override fun perform(uiController: UiController, view: View) {
            try {
                IdlingRegistry.getInstance().register(callback)
                view.addOnAttachStateChangeListener(callback)
                uiController.loopMainThreadUntilIdle()
            } finally {
                IdlingRegistry.getInstance().unregister(callback)
                view.removeOnAttachStateChangeListener(callback)
            }
        }
    }
}

private class ViewDismissCallback : IdlingResource, View.OnAttachStateChangeListener {

    private lateinit var callback: ResourceCallback
    private var dismissed = false

    override fun getName() = "View dismiss callback"

    override fun isIdleNow() = dismissed

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.callback = callback
    }

    override fun onViewDetachedFromWindow(v: View) {
        dismissed = true
        callback.onTransitionToIdle()
    }

    override fun onViewAttachedToWindow(v: View) {
    }
}