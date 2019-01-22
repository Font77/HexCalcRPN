package com.alloydflanagan.hexcalcrpn.model

import org.junit.Test

import org.junit.Assert.*
import java.math.BigInteger

class ExtensionsKtTest {

    private fun assertStrings(value: Long, regStr: String, spaced: String) {
        val asBigInt = BigInteger.valueOf(value)
        assertEquals(regStr, asBigInt.toString())
        assertEquals(spaced, asBigInt.toSpacedString())
    }

    @Test
    fun toSpacedString() {
        assertStrings(0, "0", "0")
        assertStrings(1234, "1234", "1234")
        assertStrings(12345, "12345", "1 2345")
        assertStrings(12345678, "12345678", "1234 5678")
        assertStrings(123456789, "123456789", "1 2345 6789")
        assertStrings(1234567890, "1234567890", "12 3456 7890")
        assertStrings(12345678901, "12345678901", "123 4567 8901")
        assertStrings(123456789012, "123456789012", "1234 5678 9012")
        assertStrings(-1234, "-1234", "-1234")
        assertStrings(-12345, "-12345", "-1 2345")
        assertStrings(-12345678, "-12345678", "-1234 5678")
        assertStrings(-123456789, "-123456789", "-1 2345 6789")
        assertStrings(-1234567890, "-1234567890", "-12 3456 7890")
        assertStrings(-12345678901, "-12345678901", "-123 4567 8901")
        assertStrings(-123456789012, "-123456789012", "-1234 5678 9012")
    }
}
