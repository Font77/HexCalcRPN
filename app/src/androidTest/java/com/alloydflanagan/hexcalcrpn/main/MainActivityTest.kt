package com.alloydflanagan.hexcalcrpn.main


import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
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
import org.hamcrest.Matchers.`is`
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

    // TODO: Figure out how to write custom matcher for buttons in button rows
    private fun buttonAt(@IdRes buttonRowId: Int, buttonPosition: Int): ViewInteraction {
        return onView(
                allOf(childAtPosition(
                        allOf(withId(buttonRowId),
                                childAtPosition(
                                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), buttonPosition)),
                        0),
                        isDisplayed()))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("com.alloydflanagan.hexcalcrpn", appContext.packageName)
    }

    @Test
    fun mainActivityTest() {
//        val button_8 = onView(
//                allOf(withText("8"),
//                        childAtPosition(
//                                allOf(withId(R.id.brv_2),
//                                        childAtPosition(
//                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
//                                                4)),
//                                0),
//                        isDisplayed()))
        val button_8 = buttonAt(R.id.brv_2, 4)
        button_8.perform(click())

        val tv_current = onView(
                allOf(withId(R.id.tv_current),
                        isDisplayed()))
        tv_current.check(matches(withText("8")))

        val button_F = onView(
                allOf(withText("F"),
                        childAtPosition(
                                allOf(withId(R.id.brv_1),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                3)),
                                3),
                        isDisplayed()))
        button_F.perform(click())

        val button_A = onView(
                allOf(withText("A"),
                        childAtPosition(
                                allOf(withId(R.id.brv_2),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                4)),
                                2),
                        isDisplayed()))
        button_A.perform(click())

        val button_6 = onView(
                allOf(withText("6"),
                        childAtPosition(
                                allOf(withId(R.id.brv_3),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                2),
                        isDisplayed()))
        button_6.perform(click())

        val button_5 = onView(
                allOf(withText("5"),
                        childAtPosition(
                                allOf(withId(R.id.brv_3),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                1),
                        isDisplayed()))
        button_5.perform(click())

        val button_4 = onView(
                allOf(withText("4"),
                        childAtPosition(
                                allOf(withId(R.id.brv_3),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                0),
                        isDisplayed()))
        button_4.perform(click())

        val button_0 = onView(
                allOf(withText("0"),
                        childAtPosition(
                                allOf(withId(R.id.brv_4),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                0),
                        isDisplayed()))
        button_0.perform(click())

        val button_1 = onView(
                allOf(withText("1"),
                        childAtPosition(
                                allOf(withId(R.id.brv_4),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                1),
                        isDisplayed()))
        button_1.perform(click())

        val button_2 = onView(
                allOf(withText("2"),
                        childAtPosition(
                                allOf(withId(R.id.brv_4),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                2),
                        isDisplayed()))
        button_2.perform(click())

        val button_3 = onView(
                allOf(withText("3"),
                        childAtPosition(
                                allOf(withId(R.id.brv_4),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                3),
                        isDisplayed()))
        button_3.perform(click())
        button_4.perform(click())
        button_5.perform(click())
        button_6.perform(click())

        val button_7 = onView(
                allOf(withText("7"),
                        childAtPosition(
                                allOf(withId(R.id.brv_3),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                3),
                        isDisplayed()))
        button_7.perform(click())
        button_8.perform(click())

        val button_9 = onView(
                allOf(withText("9"),
                        childAtPosition(
                                allOf(withId(R.id.brv_2),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                4)),
                                1),
                        isDisplayed()))
        button_9.perform(click())

        button_A.perform(click())

        val button_B = onView(
                allOf(withText("B"),
                        childAtPosition(
                                allOf(withId(R.id.brv_2),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                4)),
                                3),
                        isDisplayed()))
        button_B.perform(click())

        val button_C = onView(
                allOf(withText("C"),
                        childAtPosition(
                                allOf(withId(R.id.brv_1),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                3)),
                                0),
                        isDisplayed()))
        button_C.perform(click())

        val button_D = onView(
                allOf(withText("D"),
                        childAtPosition(
                                allOf(withId(R.id.brv_1),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                3)),
                                1),
                        isDisplayed()))
        button_D.perform(click())

        val button_E = onView(
                allOf(withText("E"),
                        childAtPosition(
                                allOf(withId(R.id.brv_1),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                3)),
                                2),
                        isDisplayed()))
        button_E.perform(click())

        button_F.perform(click())

        tv_current.check(matches(withText("8FA6540123456789ABCDEF")))

        val button_Clear = onView(
                allOf(withText("c"),
                        childAtPosition(
                                allOf(withId(R.id.brv_1),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                3)),
                                5),
                        isDisplayed()))
        button_Clear.perform(click())

        tv_current.check(matches(withText("0")))
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
