package com.alloydflanagan.hexcalcrpn.main


import androidx.test.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityInfTest: MainActivityTest() {

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
        val act = mActivityTestRule.activity
        while (act.tv_current.text != "0" || act.tv_output.text != "") {
            enterKeys("c")
            if (--limit < 0) break
        }
        // force infinite mode even though it's default
        enterKeys("I")
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
        enterKeys("1234_1234_1234_1234")
        enter()

        enterKeys("FEDC_FEDC_FEDC_FEDC")
        enter()

        checkOutputIs("1234123412341234\nFEDCFEDCFEDCFEDC")
        enterKeys("+")

        checkOutputIs("11111111111111110") // overflows 64 bits
    }


    /**
     * Verify that we can do simple subtraction.
     */
    @Test
    override fun testSubtract() {
        enterKeys("1234")
        enter()

        enterKeys("ABC")
        enter()
        enterKeys("-")

        checkOutputIs("778")
    }

    @Test
    override fun testMultiply() {
        enterKeys("AC57")
        enter()

        enterKeys("FACE")
        enter()

        checkOutputIs("AC57\nFACE")

        enterKeys("*")
        checkOutputIs("A8D7A402")
    }

    @Test
    override fun testDivide() {
        enterKeys("A8D7A402")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("A8D7A402\nAC57")

        enterKeys("/")
        checkOutputIs("FACE")
    }

    @Test
    override fun testAND() {
        enterKeys("FACE")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("FACE\nAC57")

        enterKeys("&")
        checkOutputIs("A846")
    }

    @Test
    override fun testOR() {
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
    override fun testClear() {
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
    override fun testClearNull() {
        enterKeys("c") // should do nothing
        checkOutputIs("")
        checkCurrentIs("0")
    }

}
