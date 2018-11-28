package com.alloydflanagan.hexcalcrpn.main

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.alloydflanagan.hexcalcrpn.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

/**
 * A base class for test suites that test MainActivity, of which we'll need several.
 * 
 * Provides a lot of utility functionality, but no actual tests
 */
abstract class MainActivityTest {

    // useful curries for button rows
    private fun buttonInRow1(position: Int) = buttonAt(R.id.brv_1, position)

    private fun buttonInRow2(position: Int) = buttonAt(R.id.brv_2, position)
    private fun buttonInRow3(position: Int) = buttonAt(R.id.brv_3, position)
    private fun buttonInRow4(position: Int) = buttonAt(R.id.brv_4, position)
    private fun buttonInModes(position: Int) = buttonAt(R.id.brv_modes, position)

    // map buttons to chars so we can express series of presses as a simple string
    private val buttonMap = mapOf(
            '0' to buttonInRow4(0), '1' to buttonInRow4(1), '2' to buttonInRow4(2),
            '3' to buttonInRow4(3), '+' to buttonInRow4(4), '|' to buttonInRow4(5),
            '4' to buttonInRow3(0), '5' to buttonInRow3(1), '6' to buttonInRow3(2),
            '7' to buttonInRow3(3), '-' to buttonInRow3(4), '&' to buttonInRow3(5),
            '8' to buttonInRow2(0), '9' to buttonInRow2(1), 'A' to buttonInRow2(2),
            'B' to buttonInRow2(3), '*' to buttonInRow2(4), '~' to buttonInRow2(5),
            'C' to buttonInRow1(0), 'D' to buttonInRow1(1), 'E' to buttonInRow1(2),
            'F' to buttonInRow1(3), '/' to buttonInRow1(4), 'c' to buttonInRow1(5),
            // and here we see the limitations of using chars as indexes...  :)
            // 8, 16, 32 bits
            'z' to buttonInModes(0), 'y' to buttonInModes(1), 'x' to buttonInModes(2),
            // 64, infinite bits, sign
            'w' to buttonInModes(3), 'I' to buttonInModes(4), 'S' to buttonInModes(5)
    )

    // other controls
    private val buttonEntry = onView(allOf(withId(R.id.btn_equals), isDisplayed()))
    private val tvCurrent = onView(allOf(withId(R.id.tv_current), isDisplayed()))
    private val tvOutput = onView(allOf(withId(R.id.tv_output), isDisplayed()))

    protected fun checkCurrentIs(value: String): ViewInteraction = tvCurrent.check(matches(withText(value)))
    protected fun checkOutputIs(value: String): ViewInteraction = tvOutput.check(matches(withText(value)))

    /**
     * Given a string, clicks the button associated with each char in the string, in order.
     */
    protected fun enterKeys(value: String) {
        for (digit in value) {
            val button = buttonMap[digit]
            if (button != null) {
                button.perform(click())
            } else throw Exception("There is no button entry for $digit")
        }
    }

    /**
     * Clicks the entry button.
     */
    protected fun enter() {
        buttonEntry.perform(click())
    }

    /**
     * Auto-generated matcher to match child of a view by position.
     */
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

    // TODO: Figure out how to write custom matcher for buttons in button rows
    /**
     * Return interaction for the button found in button row with id `buttonRowId`, at position
     * `buttonPosition` (zero-based).
     */
    private fun buttonAt(@IdRes buttonRowId: Int, buttonPosition: Int): ViewInteraction {
        return onView(
                allOf(childAtPosition(
                        withId(buttonRowId),
                        buttonPosition),
                        isDisplayed()
                ))
    }

    abstract fun testClear()
    abstract fun testEntry()
    abstract fun testAdd()
    abstract fun testDivide()
    abstract fun testMultiply()
    abstract fun testSubtract()
    abstract fun testAND()
    abstract fun testOR()
    abstract fun testClearNull()
}
