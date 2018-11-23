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
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

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
    private fun buttonAt(@IdRes buttonRowId: Int, buttonPosition: Int): ViewInteraction {
        return onView(
                allOf(childAtPosition(
                        withId(buttonRowId),
                        buttonPosition),
                      isDisplayed()
                ))
    }

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
    fun mainActivityTest() {
        val tv_current = onView(
                allOf(withId(R.id.tv_current),
                        isDisplayed()))
        tv_current.check(matches(withText("0")))

        val buttonInRow1 = { position: Int -> buttonAt(R.id.brv_1, position) }
        val buttonC = buttonInRow1(0)
        val buttonD = buttonInRow1(1)
        val buttonE = buttonInRow1(2)
        val buttonF = buttonInRow1(3)
        val buttonClear = buttonInRow1(5)

        val buttonInRow2 = { position: Int -> buttonAt(R.id.brv_2, position) }
        val button8 = buttonInRow2(0)
        val button9 = buttonInRow2(1)
        val buttonA = buttonInRow2(2)
        val buttonB = buttonInRow2(3)

        val buttonInRow3 = { position: Int -> buttonAt(R.id.brv_3, position) }
        val button4 = buttonInRow3(0)
        val button5 = buttonInRow3(1)
        val button6 = buttonInRow3(2)
        val button7 = buttonInRow3(3)

        val buttonInRow4 = { position: Int -> buttonAt(R.id.brv_4, position) }
        val button0 = buttonInRow4(0)
        val button1 = buttonInRow4(1)
        val button2 = buttonInRow4(2)
        val button3 = buttonInRow4(3)

        button8.perform(click())

        tv_current.check(matches(withText("8")))

        buttonF.perform(click())
        buttonA.perform(click())
        button6.perform(click())
        button5.perform(click())
        button4.perform(click())
        button0.perform(click())
        button1.perform(click())
        button2.perform(click())
        button3.perform(click())
        button4.perform(click())
        button5.perform(click())
        button6.perform(click())
        button7.perform(click())
        button8.perform(click())
        button9.perform(click())
        buttonA.perform(click())
        buttonB.perform(click())
        buttonC.perform(click())
        buttonD.perform(click())
        buttonE.perform(click())
        buttonF.perform(click())

        tv_current.check(matches(withText("8FA6540123456789ABCDEF")))

        buttonClear.perform(click())

        tv_current.check(matches(withText("0")))
    }
}
