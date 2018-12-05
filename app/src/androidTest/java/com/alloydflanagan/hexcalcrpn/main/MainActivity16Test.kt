package com.alloydflanagan.hexcalcrpn.main


import androidx.test.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivity16Test: MainActivityTest() {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * clears both current value and stack outputs. Prevents failed test from affecting others.
     */
    @Before
    fun clearOutputs() {
        /** guard against infinite loop if other checks fail */
//        var limit = 15
//        val act = mActivityTestRule.activity
//        while (act.tv_current.text != "0" || act.tv_output.text != "") {
//            enterKeys("c")
//            if (--limit < 0) break
//        }
        // set 16-bit mode
        enterKeys("y")
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
        enterKeys("57")
        enter()

        enterKeys("CE")
        enter()

        checkOutputIs("57\nCE")

        enterKeys("*")
        checkOutputIs("4602")

        enterKeys("2345")
        enter()
        enterKeys("*")
        checkOutputIs("248A")
    }

    @Test
    override fun testDivide() {
        enterKeys("A8D7A402")
        enter()
        enterKeys("57")
        enter()

        checkOutputIs("A402\n57")

        enterKeys("/")
        checkOutputIs("1E2")
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
