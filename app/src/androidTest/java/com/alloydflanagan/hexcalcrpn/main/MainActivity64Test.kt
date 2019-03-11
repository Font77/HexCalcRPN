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
class MainActivity64Test: MainActivityUI(), MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * clears both current value and stack outputs. Prevents failed test from affecting others.
     */
    @BeforeTest
    fun setup() {
        // enter 64-bit mode, clear input and output as side effect
        enterKeys("w")
        testSetup()
    }

    /**
     * Verify that pressing a series of digits creates a number in the current value register,
     * and that pressing "clear" resets that number to 0.
     */
    @Test
    override fun testEntry() {
        enterKeys("8")
        checkCurrentIs("8")
        enterKeys("F_A654_0123_4567_89AB_CDEF")

        checkCurrentIs("8F A654 0123 4567 89AB CDEF")

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

        checkOutputIs("1234 1234 1234 1234\nFEDC FEDC FEDC FEDC")
        enterKeys("+")

        checkOutputIs("1111 1111 1111 1110") // drop 1 from overflow
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

        enterKeys("ABC")
        enter()
        enterKeys("-")

        // 0x778 - 0xABC == -0x344
        // 1100 1011 1100 ==> 0xCBC -- two's complement
        // 2's complement in 64 bits ==>
        checkOutputIs("FFFF FFFF FFFF FCBC")
    }

    @Test
    override fun testMultiply() {
        enterKeys("5FD_9A8F")
        enter()

        enterKeys("2A_BBBB_BB91")
        enter()

        checkOutputIs("5FD 9A8F\n2A BBBB BB91")

        enterKeys("*")
        checkOutputIs("FFFF FFFF FFFF FFFF")
    }

    @Test
    override fun testDivide() {
        enterKeys("FFFF_FFFF_FFFF_FFFF")
        enter()
        enterKeys("2A_BBBB_BB91")
        enter()

        checkOutputIs("FFFF FFFF FFFF FFFF\n2A BBBB BB91")

        enterKeys("/")
        checkOutputIs("5FD 9A8F")
    }

    @Test
    override fun testAND() {
        enterKeys("12FA_CE0F_F0FF_FACE")
        enter()
        enterKeys("BADB_ADBA_DBAD_BEEF")
        enter()

        checkOutputIs("12FA CE0F F0FF FACE\nBADB ADBA DBAD BEEF")

        enterKeys("&")
        checkOutputIs("12DA 8C0A D0AD BACE")
    }

    @Test
    override fun testOR() {
        enterKeys("12FA_CE0F_F0FF_FACE")
        enter()
        enterKeys("BADB_ADBA_DBAD_BEEF")
        enter()

        checkOutputIs("12FA CE0F F0FF FACE\n" +
                "BADB ADBA DBAD BEEF")

        enterKeys("|")
        checkOutputIs("BAFB EFBF FBFF FEEF")
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
        enterKeys("0123_4567_89AB_CDEF")
        enter()
        checkOutputIs("123 4567 89AB CDEF")
        enterKeys("~")
        checkOutputIs("FEDC BA98 7654 3210")
        enterKeys("~")
        checkOutputIs("123 4567 89AB CDEF")
        enterKeys("cF")
        enter()
        enterKeys("~")
        checkOutputIs("FFFF FFFF FFFF FFF0")
        enterKeys("c")
        enter()
        enterKeys("~")
        checkOutputIs("FFFF FFFF FFFF FFFF")
    }

    @Test
    override fun test2sComplement() {
        enterKeys("123_4567_89AB_CDEF")
        enter()
        checkOutputIs("123 4567 89AB CDEF")
        enterKeys("s")
        checkOutputIs("FEDC BA98 7654 3211")
        enterKeys("s")
        checkOutputIs("123 4567 89AB CDEF")
        enterKeys("cF")
        enter()
        enterKeys("s")
        checkOutputIs("FFFF FFFF FFFF FFF1")
        enterKeys("c")
        enter()
        enterKeys("s")
        checkOutputIs("0")
    }

    @Test
    override fun testInvertCurrent() {
        enterKeys("0123_4567_89AB_CDEF")
        checkCurrentIs("123 4567 89AB CDEF")
        enterKeys("~")
        checkCurrentIs("FEDC BA98 7654 3210")
        enterKeys("~")
        checkCurrentIs("123 4567 89AB CDEF")
        enterKeys("cF")
        enterKeys("~")
        checkCurrentIs("FFFF FFFF FFFF FFF0")
        enterKeys("c0~")
        checkCurrentIs("FFFF FFFF FFFF FFFF")
    }

    @Test
    override fun test2sCompCurrent() {
        enterKeys("123_4567_89AB_CDEF")
        checkCurrentIs("123 4567 89AB CDEF")
        enterKeys("s")
        checkCurrentIs("FEDC BA98 7654 3211")
        enterKeys("s")
        checkCurrentIs("123 4567 89AB CDEF")
        enterKeys("cFs")
        checkCurrentIs("FFFF FFFF FFFF FFF1")
        enterKeys("cs")
        checkCurrentIs("0")
    }
}
