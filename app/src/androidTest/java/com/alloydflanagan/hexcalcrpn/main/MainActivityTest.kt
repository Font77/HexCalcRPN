package com.alloydflanagan.hexcalcrpn.main


import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alloydflanagan.hexcalcrpn.R
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    // useful curries for button rows
    private fun buttonInRow1(position: Int) =  buttonAt(R.id.brv_1, position)
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
            'F' to buttonInRow1(3), '/' to buttonInRow1(4), 'c' to buttonInRow1(5)
    )

    // other controls
    private val buttonEntry = onView(allOf(withId(R.id.btn_equals), isDisplayed()))
    private val tvCurrent = onView(allOf(withId(R.id.tv_current), isDisplayed()))
    private val tvOutput = onView(allOf(withId(R.id.tv_output), isDisplayed()))

    private fun checkCurrentIs(value: String) = tvCurrent.check(matches(withText(value)))
    private fun checkOutputIs(value: String) = tvOutput.check(matches(withText(value)))

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * Given a string, clicks the button associated with each char in the string, in order.
     */
    private fun enterKeys(value: String) {
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
    private fun enter () {
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

    /**
     * clears both current value and stack outputs. Prevents failed test from affecting others.
     */
    @Before
    fun clearOutputs() {
        /** guard against infinite loop if other checks fail */
        var limit = 15
        while (mActivityTestRule.activity.tv_current.text != "0" || mActivityTestRule.activity.tv_output.text != "") {
            enterKeys("c")
            if (--limit < 0) break
        }
    }

    /**
     * Verify we can get application context, and it is what I think it is.
     */
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("com.alloydflanagan.hexcalcrpn", appContext.packageName)
    }

    /**
     * Verify that pressing a series of digits creates a number in the current value register,
     * and that pressing "clear" resets that number to 0.
     */
    @Test
    fun testEntry() {
        enterKeys("8")
        checkCurrentIs("8")
        enterKeys("FA6540123456789ABCDEF")

        checkCurrentIs("8FA6540123456789ABCDEF")

        enterKeys("c")
        checkCurrentIs("0")
    }

    /**
     * Verify that we can do simple addition.
     */
    @Test
    fun testAdd() {
        enterKeys("1234")
        enter()

        enterKeys("ABC")
        enter()

        enterKeys("+")

        checkOutputIs("1CF0")
    }

    /**
     * Verify that we can do simple subtraction.
     */
    @Test
    fun testSubtract() {
        enterKeys("1234")
        enter()

        enterKeys("ABC")
        enter()
        enterKeys("-")

        checkOutputIs("778")
    }

    @Test
    fun testMultiply() {
        enterKeys("AC57")
        enter()

        enterKeys("FACE")
        enter()

        checkOutputIs("AC57\nFACE")

        enterKeys("*")
        checkOutputIs("A8D7A402")
    }

    @Test
    fun testDivide() {
        enterKeys("A8D7A402")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("A8D7A402\nAC57")

        enterKeys("/")
        checkOutputIs("FACE")
    }

    @Test
    fun testAND() {
        enterKeys("FACE")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("FACE\nAC57")

        enterKeys("&")
        checkOutputIs("A846")
    }

    @Test
    fun testOR() {
        enterKeys("FACE")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("FACE\nAC57")

        enterKeys("|")
        checkOutputIs("FEDF")
    }

    /**
     * Verify that pressing "c" clears the current value if any, otherwise removes one entry from
     * stack.
     */
    @Test
    fun testClear() {
        enterKeys("1234")
        enter()
        checkCurrentIs("0")
        checkOutputIs("1234")

        enterKeys("ABC")
        enter()
        checkCurrentIs("0")
        checkOutputIs("1234\nABC")

        enterKeys("747")
        checkCurrentIs("747")

        enterKeys("c")

        checkCurrentIs("0")
        checkOutputIs("1234\nABC")

        enterKeys("c")

        checkOutputIs("1234")

        enterKeys("c")

        checkOutputIs("")
    }

    /**
     * Verify pressing "clear" with no input and no stack does nothing.
     */
    @Test
    fun testClearNull() {
        enterKeys("c") // should do nothing
        checkOutputIs("")
        checkCurrentIs("0")
    }

}
