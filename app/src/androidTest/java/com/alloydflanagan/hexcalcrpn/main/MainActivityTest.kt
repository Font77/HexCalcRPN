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
    private fun buttonInDigits0(position: Int) = buttonAt(R.id.brv_digits_0, position)
    private fun buttonInDigits4(position: Int) = buttonAt(R.id.brv_digits_4, position)
    private fun buttonInDigits8(position: Int) = buttonAt(R.id.brv_digits_8, position)
    private fun buttonInDigitsC(position: Int) = buttonAt(R.id.brv_digits_C, position)
    private fun buttonInOps0(position: Int) = buttonAt(R.id.brv_op_0, position)
    private fun buttonInOps1(position: Int) = buttonAt(R.id.brv_op_1, position)
    private fun buttonInOps2(position: Int) = buttonAt(R.id.brv_op_2, position)
    private fun buttonInOps3(position: Int) = buttonAt(R.id.brv_op_3, position)
    private fun buttonInOps4(position: Int) = buttonAt(R.id.brv_op_4, position)

    // but word size buttons are special case
    private fun modeRadioButton(@IdRes btnId: Int): ViewInteraction {
        return onView(
                allOf(withId(btnId),
                        isDisplayed()))
    }

    private fun word8() = modeRadioButton(R.id.radio_8)
    private fun word16() = modeRadioButton(R.id.radio_16)
    private fun word32() = modeRadioButton(R.id.radio_32)
    private fun word64() = modeRadioButton(R.id.radio_64)
    private fun wordInf() = modeRadioButton(R.id.radio_inf)

    // map buttons to chars so we can express series of presses as a simple string
    private val buttonMap = mapOf(
            '0' to buttonInDigits0(0), '1' to buttonInDigits0(1), '2' to buttonInDigits0(2),
            '3' to buttonInDigits0(3), '4' to buttonInDigits4(0), '5' to buttonInDigits4(1),
            '6' to buttonInDigits4(2), '7' to buttonInDigits4(3), '8' to buttonInDigits8(0),
            '9' to buttonInDigits8(1), 'A' to buttonInDigits8(2), 'B' to buttonInDigits8(3),
            'C' to buttonInDigitsC(0), 'D' to buttonInDigitsC(1), 'E' to buttonInDigitsC(2),
            'F' to buttonInDigitsC(3),

            // and here we see the limitations of using chars as indexes...  :)
            'z' to word8(), 'y' to word16(), 'x' to word32(), 'w' to word64(), 'I' to wordInf(),

            'c' to buttonInOps0(0), 'e' to buttonInOps0(1),
            '~' to buttonInOps1(0), 's' to buttonInOps1(1),
            '&' to buttonInOps2(0), '|' to buttonInOps2(1),
            '*' to buttonInOps3(0), '/' to buttonInOps3(1),
            '+' to buttonInOps4(0), '-' to buttonInOps4(1)
    )

    // other controls
    private val buttonEntry = onView(allOf(withId(R.id.btn_equals), isDisplayed()))
    private val tvCurrent = onView(allOf(withId(R.id.tv_current), isDisplayed()))
    private val tvOutput = onView(allOf(withId(R.id.tv_output), isDisplayed()))

    protected fun checkCurrentIs(value: String): ViewInteraction = tvCurrent.check(matches(withText(value.replace("_", ""))))
    protected fun checkOutputIs(value: String): ViewInteraction {
        val fixed = value.replace("_", "")
        return tvOutput.check(matches(withText(fixed)))
    }

    /**
     * Given a string, clicks the button associated with each char in the string, in order.
     *
     * The value '_' in the string is ignored, so it can be used as a separator
     */
    protected fun enterKeys(value: String) {
        for (digit in value) {
            if (digit == '_') continue
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
    abstract fun testInvert()
    abstract fun test2sComplement()
    abstract fun testClearNull()
    abstract fun testInvertCurrent()
    abstract fun test2sCompCurrent()
}
