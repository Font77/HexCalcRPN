package com.alloydflanagan.hexcalcrpn.main


import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.AppPreferences
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@LargeTest
@RunWith(AndroidJUnit4::class)
class DigitsSepPreferenceTests {

    private lateinit var prefs: AppPreferences

    private lateinit var packageContext: Context

    // test rule for main activity. We do NOT launch it yet.
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @BeforeTest
    fun setupPrefs() {
        packageContext = InstrumentationRegistry.getInstrumentation().targetContext
        prefs = AppPreferences(packageContext)
        prefs.prefDigitSep = false
    }

    private fun clickSettingsAction() {
        val actionMenuItemView = onView(
                allOf(withId(R.id.action_settings),
                        withContentDescription("Settings"),
                        isDisplayed()))
        actionMenuItemView.perform(click())
    }

    /**
     * Returns the settings preference item at index `position`.
     */
    private fun settingAtIndex(position: Int): ViewInteraction {
        return onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.recycler_view),
                                position),
                        2),
                        isDisplayed()))
    }

    /**
     * Verify that when the user turns on the digits separator preference in the settings
     * activity, that preference change is remembered.
     */
    @Test
    fun saveDigitsSeparatorPref() {
        mActivityTestRule.launchActivity(Intent())

        clickSettingsAction()

        val prefTitle = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view),
                                        1),
                                1),
                        0),
                        isDisplayed()))

        prefTitle.check(matches(withText(packageContext.getString(R.string.pref_digits_sep_title))))

        val digitSepSwitch = onView(
                allOf(withId(R.id.switchWidget),
                        childAtPosition(
                                allOf(withId(android.R.id.widget_frame),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                                2)),
                                0),
                        isDisplayed()))
        digitSepSwitch.check(matches(isDisplayed()))
        digitSepSwitch.check(matches(not(isChecked())))

        val linearLayout = onView(
                allOf(
                        childAtPosition(
                                allOf(withId(R.id.recycler_view)),
                                1
                        ),
                        isDisplayed()
                ))

        linearLayout.check(matches(withClassName(`is`(android.widget.LinearLayout::class.java.canonicalName))))

        linearLayout.perform(click())
        digitSepSwitch.check(matches(isChecked()))

        pressBack()

        assertEquals(true, prefs.prefDigitSep)

    }

    /**
     * Verify that when the user turns off the digits separator preference in the settings
     * activity, that preference change is remembered.
     */
    @Test
    fun saveDigitsSeparatorPrefFalse() {
        prefs.prefDigitSep = true
        mActivityTestRule.launchActivity(Intent())

        clickSettingsAction()

        val prefTitle = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view),
                                        1),
                                1),
                        0),
                        isDisplayed()))

        prefTitle.check(matches(withText(packageContext.getString(R.string.pref_digits_sep_title))))

        val digitSepSwitch = onView(
                allOf(withId(R.id.switchWidget),
                        childAtPosition(
                                allOf(withId(android.R.id.widget_frame),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                                2)),
                                0),
                        isDisplayed()))
        digitSepSwitch.check(matches(isDisplayed()))
        digitSepSwitch.check(matches(isChecked()))

        val linearLayout = onView(
                allOf(
                        childAtPosition(
                                allOf(withId(R.id.recycler_view)),
                                1
                        ),
                        isDisplayed()
                ))

        linearLayout.check(matches(withClassName(`is`(android.widget.LinearLayout::class.java.canonicalName))))

        linearLayout.perform(click())
        digitSepSwitch.check(matches(not(isChecked())))

        pressBack()

        assertEquals(false, prefs.prefDigitSep)

    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
