@file:Suppress("SpellCheckingInspection")

package com.alloydflanagan.hexcalcrpn.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Assert
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("SpellCheckingInspection")
@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivity16Test: MainActivityUI(), MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * clears both current value and stack outputs. Prevents failed test from affecting others.
     */
    @BeforeTest
    fun clearOutputs() {
        // set 16-bit mode
        enterKeys("y")
        testSetup()
    }

    /**
     * Verify we can get application context, and it is what I think it is.
     */
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
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

        checkCurrentIs("8F A654 0123 4567 89AB CDEF")

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

    @Test
    override fun testInvert() {
        enterKeys("ABCD")
        enter()
        checkOutputIs("ABCD")
        enterKeys("~")
        checkOutputIs("5432")
        enterKeys("~")
        checkOutputIs("ABCD")
        enterKeys("c")
        enter()
        checkOutputIs("0")
        enterKeys("~")
        checkOutputIs("FFFF")
    }

    @Test
    override fun test2sComplement() {
        enterKeys("ABCD")
        enter()
        checkOutputIs("ABCD")
        enterKeys("s")
        checkOutputIs("5433")
        enterKeys("s")
        checkOutputIs("ABCD")
        enterKeys("c")
        enter()
        checkOutputIs("0")
        enterKeys("s")
        checkOutputIs("0")
    }

    @Test
    override fun testInvertCurrent() {
        enterKeys("ABCD")
        enterKeys("~")
        checkCurrentIs("5432")
        checkOutputIs("")
        enterKeys("~")
        checkCurrentIs("ABCD")
        checkOutputIs("")
        enterKeys("c")
        checkCurrentIs("0")
        checkOutputIs("")
        enterKeys("0~")
        checkCurrentIs("FFFF")
        checkOutputIs("")
    }

    @Test
    override fun test2sCompCurrent() {
        enterKeys("ABCD")
        enterKeys("s")
        checkCurrentIs("5433")
        enterKeys("s")
        checkCurrentIs("ABCD")
        enterKeys("c")
        checkCurrentIs("0")
        enterKeys("0s")
        checkCurrentIs("0")
        checkOutputIs("")
    }
}
