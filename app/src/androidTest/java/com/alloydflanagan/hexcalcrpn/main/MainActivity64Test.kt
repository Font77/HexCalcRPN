package com.alloydflanagan.hexcalcrpn.main


import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivity64Test: MainActivityTest() {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * clears both current value and stack outputs. Prevents failed test from affecting others.
     */
    @Before
    fun clearOutputs() {
        // enter 64-bit mode, clear input and output as side effect
        enterKeys("w")
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

        checkOutputIs("1111111111111110") // drop 1 from overflow
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
        checkOutputIs("FFFFFFFFFFFFFCBC")
    }

    @Test
    override fun testMultiply() {
        enterKeys("5FD_9A8F")
        enter()

        enterKeys("2A_BBBB_BB91")
        enter()

        checkOutputIs("5FD9A8F\n2ABBBBBB91")

        enterKeys("*")
        checkOutputIs("FFFFFFFFFFFFFFFF")
    }

    @Test
    override fun testDivide() {
        enterKeys("FFFF_FFFF_FFFF_FFFF")
        enter()
        enterKeys("2A_BBBB_BB91")
        enter()

        checkOutputIs("FFFFFFFFFFFFFFFF\n2ABBBBBB91")

        enterKeys("/")
        checkOutputIs("5FD9A8F")
    }

    @Test
    override fun testAND() {
        enterKeys("12FA_CE0F_F0FF_FACE")
        enter()
        enterKeys("BADB_ADBA_DBAD_BEEF")
        enter()

        checkOutputIs("12FACE0FF0FFFACE\nBADBADBADBADBEEF")

        enterKeys("&")
        checkOutputIs("12DA8C0AD0ADBACE")
    }

    @Test
    override fun testOR() {
        enterKeys("12FA_CE0F_F0FF_FACE")
        enter()
        enterKeys("BADB_ADBA_DBAD_BEEF")
        enter()

        checkOutputIs("12FACE0FF0FFFACE\n" +
                "BADBADBADBADBEEF")

        enterKeys("|")
        checkOutputIs("BAFBEFBFFBFFFEEF")
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
