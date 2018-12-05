package com.alloydflanagan.hexcalcrpn.main


import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivity8Test:  MainActivityTest() {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

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
        enterKeys("z") // 8-bit mode
    }

    /**
     * Verify that pressing a series of digits creates a number in the current value register,
     * and that pressing "clear" resets that number to 0.
     */
    @Test
    override fun testEntry() {
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
    override fun testAdd() {
        enterKeys("1234")
        enter()
        checkOutputIs("34")

        enterKeys("ABCD")  // "CD"
        enter()

        enterKeys("+")

        checkOutputIs("1") // 101 truncated
    }

    /**
     * Verify that we can do simple subtraction.
     */
    @Test
    override fun testSubtract() {
        enterKeys("ABC")  // BC
        enter()

        enterKeys("12")
        enter()
        enterKeys("-")

        checkOutputIs("AA")

        enterKeys("BC")
        enter()
        enterKeys("-")
        // result is -18
        // 18 ==> 10010
        // 8-bit complement ==> 11101101
        // two's complement ==> 11101110
        checkOutputIs("EE")
    }

    @Test
    override fun testMultiply() {
        enterKeys("AC57")
        enter()

        enterKeys("FACE")
        enter()

        checkOutputIs("57\nCE")

        enterKeys("*")
        checkOutputIs("2")
    }

    @Test
    override fun testDivide() {
        enterKeys("A488")
        enter()
        enterKeys("2")
        enter()

        checkOutputIs("88\n2")

        enterKeys("/")
        checkOutputIs("44")
    }

    @Test
    override fun testAND() {
        enterKeys("FACE")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("CE\n57")

        enterKeys("&")
        checkOutputIs("46")
    }

    @Test
    override fun testOR() {
        enterKeys("FACE")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("CE\n57")

        enterKeys("|")
        checkOutputIs("DF")
    }

    /**
     * Verify that pressing "c" clears the current value if any, otherwise removes one entry from
     * stack.
     */
    @Test
    override fun testClear() {
        enterKeys("1234")
        enter()
        checkCurrentIs("0")
        checkOutputIs("34")

        enterKeys("ABC")
        enter()
        checkCurrentIs("0")
        checkOutputIs("34\nBC")

        enterKeys("747")
        checkCurrentIs("747")

        enterKeys("c")

        checkCurrentIs("0")
        checkOutputIs("34\nBC")

        enterKeys("c")

        checkOutputIs("34")

        enterKeys("c")

        checkOutputIs("")
    }

    /**
     * Verify pressing "clear" with no input and no stack does nothing.
     */
    @Test
    override fun testClearNull() {
        enterKeys("c") // should do nothing
        checkOutputIs("")
        checkCurrentIs("0")
    }

}
