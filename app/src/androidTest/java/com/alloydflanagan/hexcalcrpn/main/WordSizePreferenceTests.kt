package com.alloydflanagan.hexcalcrpn.main


import android.content.Intent
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.AppPreferences
import com.alloydflanagan.hexcalcrpn.model.BitsPreference
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@LargeTest
@RunWith(AndroidJUnit4::class)
class WordSizePreferenceTests {

    private lateinit var prefs: AppPreferences

    // test rule for main activity. We do NOT launch it yet.
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    // match radio button with a given id at `position` in the size fragment.
    private fun radioButtonAt(@IdRes anId: Int) = onView(
            allOf(withId(anId),
                    isDisplayed()))

    /**
     * Verifies that when launched with 'last_word_size' preference set to `lastSize`,
     * the activity will check the radio button `button`.
     */
    private fun verifyInitialWordSize(lastSize: BitsPreference, @IdRes button: Int) {
        prefs.lastWordSize = lastSize
        mActivityTestRule.launchActivity(Intent())
        assertLastSizeIs(lastSize)  // safety check
        val radioButton = radioButtonAt(button)
        radioButton.check(matches(isChecked()))
    }

    @BeforeTest
    fun setupPrefs() {
        val packageContext = InstrumentationRegistry.getInstrumentation().targetContext
        prefs = AppPreferences(packageContext)
        prefs.prefInitSize = BitsPreference.PREVIOUS
    }

    /**
     * Verify that when the preference for initial word size is [BitsPreference.PREVIOUS], the
     * initial word size is set to the value of the last_word_size preference.
     */
    @Test
    fun useLastWordSizeTest8() {
        verifyInitialWordSize(BitsPreference.EIGHT, R.id.radio_8)
    }

    @Test
    fun useLastWordSizeTest16() {
        verifyInitialWordSize(BitsPreference.SIXTEEN, R.id.radio_16)
    }

    @Test
    fun useLastWordSizeTest32() {
        verifyInitialWordSize(BitsPreference.THIRTY_TWO, R.id.radio_32)
    }

    @Test
    fun useLastWordSizeTest64() {
        verifyInitialWordSize(BitsPreference.SIXTY_FOUR, R.id.radio_64)
    }

    @Test
    fun useLastWordSizeTestInf() {
        verifyInitialWordSize(BitsPreference.INFINITE, R.id.radio_inf)
    }

    /**
     * Verify that when the user changes the word size in the activity, the preference setting
     * to remember it is updated.
     */
    @Test
    fun saveLastWordSizeTest() {
        prefs.lastWordSize = BitsPreference.THIRTY_TWO
        mActivityTestRule.launchActivity(Intent())

        assertInitPrefIs(BitsPreference.PREVIOUS)
        assertLastSizeIs(BitsPreference.THIRTY_TWO)

        val radioButton8 = radioButtonAt(R.id.radio_8)
        val radioButton16 = radioButtonAt(R.id.radio_16)
        val radioButton32 = radioButtonAt(R.id.radio_32)
        val radioButton64 = radioButtonAt(R.id.radio_64)
        val radioButtonInf = radioButtonAt(R.id.radio_inf)

        radioButton8.perform(click())
        radioButton8.check(matches(isChecked()))
        assertLastSizeIs(BitsPreference.EIGHT)

        radioButton16.perform(click())
        radioButton16.check(matches(isChecked()))
        assertLastSizeIs(BitsPreference.SIXTEEN)

        radioButton32.perform(click())
        radioButton32.check(matches(isChecked()))
        assertLastSizeIs(BitsPreference.THIRTY_TWO)

        radioButton64.perform(click())
        radioButton64.check(matches(isChecked()))
        assertLastSizeIs(BitsPreference.SIXTY_FOUR)

        radioButtonInf.perform(click())
        radioButtonInf.check(matches(isChecked()))
        assertLastSizeIs(BitsPreference.INFINITE)
    }

    /**
     * Assert that the 'last_word_size' preference is set to `expected`.
     */
    private fun assertLastSizeIs(expected: BitsPreference) {
        assertThat(prefs.lastWordSize, PrefsMatcher(expected))
        assertEquals(expected, prefs.lastWordSize)
    }

    /**
     * Assert that the 'init_size' preference is set to `expected`.
     */
    private fun assertInitPrefIs(expected: BitsPreference) {
        assertEquals(expected, prefs.prefInitSize)
    }

    /**
     * Simplest possible matcher to verify that a BitsPreference value is equal to the expected
     * value.
     */
    class PrefsMatcher(private val expected: BitsPreference): TypeSafeMatcher<BitsPreference>() {
        /**
         * Generates a description of the {@link BitsPreference} value.
         *
         * @param description
         * The description to be built or appended to.
         */
        override fun describeTo(description: Description) {
            description.appendText("<$expected>")
        }

        /**
         * @return Boolean true if item equals the expected value.
         */
        override fun matchesSafely(item: BitsPreference?): Boolean = item == expected
    }
}
