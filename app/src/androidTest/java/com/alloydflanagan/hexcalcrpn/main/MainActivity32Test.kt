package com.alloydflanagan.hexcalcrpn.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.BeforeTest

@Suppress("SpellCheckingInspection")
@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivity32Test: MainActivityUI(), MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * clears both current value and stack outputs. Prevents failed test from affecting others.
     */
    @BeforeTest
    fun setup() {
        // 32-bit mode, also clears input and stack
        enterKeys("x")
        testSetup(false) // test w/out seps just to mix it up
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
        enterKeys("123456789") // 23456789
        enter()

        enterKeys("ABCDEF0123") // CDEF0123
        enter()

        enterKeys("+")

        checkOutputIs("F13468AC")

        enterKeys("123456789") // 23456789
        enter()
        enterKeys("+")

        checkOutputIs("1479D035")

    }

    /**
     * Verify that we can do simple subtraction.
     */
    @Test
    override fun testSubtract() {
        enterKeys("123456789AB")  // 456789AB
        enter()

        enterKeys("ABCDE")
        enter()
        enterKeys("-")

        checkOutputIs("455CCCCD")
    }

    @Test
    override fun testMultiply() {
        enterKeys("CABB1E")
        enter()

        enterKeys("FACE0FF")
        enter()

        checkOutputIs("CABB1E\nFACE0FF")

        enterKeys("*")
        checkOutputIs("A7D2A2E2")
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
        enterKeys("CABB1E")
        enter()
        enterKeys("FACE0FF")
        enter()

        checkOutputIs("CABB1E\nFACE0FF")

        enterKeys("&")
        checkOutputIs("88A01E")
    }

    @Test
    override fun testOR() {
        enterKeys("FACE0FF")
        enter()
        enterKeys("CABB1E")
        enter()

        checkOutputIs("FACE0FF\nCABB1E")

        enterKeys("|")
        checkOutputIs("FEEFBFF")
    }

    /**
     * Verify that pressing "c" clears the current value if any, otherwise removes one entry from
     * stack.
     */
    @Test
    override fun testClear() {
        enterKeys("1234FACE0FF")
        checkCurrentIs("1234FACE0FF")
        enter()
        checkCurrentIs("0")
        checkOutputIs("4FACE0FF")

        enterKeys("ABACABB1E") // https://www.youtube.com/watch?v=QbjfesCI254
        enter()
        checkCurrentIs("0")
        checkOutputIs("4FACE0FF\nBACABB1E")

        enterKeys("747ABACAB")
        checkCurrentIs("747ABACAB")

        enterKeys("c")

        checkCurrentIs("0")
        checkOutputIs("4FACE0FF\nBACABB1E")

        enterKeys("c")

        checkOutputIs("4FACE0FF")

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
        enterKeys("89AB_CDEF")
        enter()
        checkOutputIs("89AB_CDEF")
        enterKeys("~")
        checkOutputIs("7654_3210")
        enterKeys("~")
        checkOutputIs("89AB_CDEF")
        enterKeys("cF")
        enter()
        enterKeys("~")
        checkOutputIs("FFFF_FFF0")
        enterKeys("c")
        enter()
        checkOutputIs("0")
        enterKeys("~")
        checkOutputIs("FFFF_FFFF")
    }

    @Test
    override fun test2sComplement() {
        enterKeys("89AB_CDEF")
        enter()
        checkOutputIs("89AB_CDEF")
        enterKeys("s")
        checkOutputIs("7654_3211")
        enterKeys("s")
        checkOutputIs("89AB_CDEF")
        enterKeys("cF")
        enter()
        enterKeys("s")
        checkOutputIs("FFFF_FFF1")
        enterKeys("c")
        enter()
        checkOutputIs("0")
        enterKeys("s")
        checkOutputIs("0")
    }

    @Test
    override fun testInvertCurrent() {
        enterKeys("89AB_CDEF")
        enterKeys("~")
        checkCurrentIs("7654_3210")
        checkOutputIs("")
        enterKeys("~")
        checkCurrentIs("89AB_CDEF")
        checkOutputIs("")
        enterKeys("c")
        checkCurrentIs("0")
        checkOutputIs("")
        enterKeys("0~")
        checkCurrentIs("FFFF_FFFF")
        checkOutputIs("")
    }

    @Test
    override fun test2sCompCurrent() {
        enterKeys("89AB_CDEF")
        enterKeys("s")
        checkCurrentIs("7654_3211")
        enterKeys("s")
        checkCurrentIs("89AB_CDEF")
        enterKeys("c")
        checkCurrentIs("0")
        enterKeys("0s")
        checkCurrentIs("0")
        checkOutputIs("")
    }
}
