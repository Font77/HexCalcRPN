package com.alloydflanagan.hexcalcrpn.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alloydflanagan.hexcalcrpn.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingActivityTest1 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun settingActivityTest1() {
        val actionMenuItemView = onView(
                allOf(withId(R.id.action_settings),
                        withContentDescription("Settings"),
                        isDisplayed()))
        actionMenuItemView.perform(click())

        val linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.recycler_view),
                                childAtPosition(
                                        allOf(withId(android.R.id.list_container),
                                                childAtPosition(
                                                        withClassName(`is`("android.widget.LinearLayout")),
                                                        0)),
                                        0)),
                        0),
                        isDisplayed()))

//        val textView = onView(
//                allOf(withId(android.R.id.summary),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
//                                        0),
//                                1),
//                        isDisplayed()))
//
//        val res = mActivityTestRule.activity.resources.getStringArray(R.array.size_labels)
//        //val expected = "Currently: ${res[2]}\n${R.string.pref_init_size_desc}"
//        val expected = "Currently: 32 bits\nThe word size set on startup."
//        textView.check(matches(withText(expected)))

        val textView2 = onView(
                allOf(withId(android.R.id.title),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        // textView2.check(matches(withText(R.string.pref_init_size_title)))
        textView2.check(matches(isDisplayed()))

        linearLayout.perform(click())

        val checkedTextView = onView(
                allOf(withId(android.R.id.text1),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        childAtPosition(
                                                withId(R.id.contentPanel),
                                                0)),
                                2),
                        isDisplayed()))
        checkedTextView.check(matches(isDisplayed()))

        val appCompatCheckedTextView_64bits = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                allOf(withId(R.id.contentPanel),
                                        childAtPosition(
                                                allOf(withId(R.id.parentPanel),
                                                        childAtPosition(
                                                                allOf(withId(android.R.id.content),
                                                                        childAtPosition(
                                                                                withId(R.id.action_bar_root),
                                                                                0)),
                                                                0)),
                                                1)),
                                0)))
                .atPosition(3)
        appCompatCheckedTextView_64bits.perform(click())

        val tv64bits = onView(
                allOf(withId(android.R.id.summary),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                        0),
                                1),
                        isDisplayed()))
        tv64bits.check(matches(withText("Currently: 64 bits The word size set on startup.")))
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
