package com.alloydflanagan.hexcalcrpn

import org.junit.Test

import java.math.BigInteger

import org.junit.Assert.assertEquals

/**
 * Test the utility class HexStack.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HexStackUnitTest {

    @Test
    fun constructFromCollection() {

    }

    @Test
    fun addition_isCorrect() {
        val fred = HexStack()
        fred.push(BigInteger.valueOf(7))
        fred.push(BigInteger.valueOf(3))
        fred.add()
        assertEquals(fred.size(), 1)
        assertEquals(fred.pop(), BigInteger.valueOf(10))
    }

    @Test
    fun subtraction_isCorrect() {
        val wilma = HexStack()
        wilma.push(BigInteger.valueOf(7))
        wilma.push(BigInteger.valueOf(3))
        wilma.subtract()
        assertEquals(wilma.size(), 1)
        assertEquals(wilma.pop(), BigInteger.valueOf(4))
    }

    @Test
    fun multiply_isCorrect() {
        val pebbles = HexStack()
        pebbles.push(BigInteger.valueOf(3))
        pebbles.push(BigInteger.valueOf(5))
        pebbles.multiply()
        assertEquals(pebbles.size(), 1)
        assertEquals(pebbles.peek(), BigInteger.valueOf(15))
        pebbles.push(BigInteger.valueOf(-5))
        pebbles.multiply()
        assertEquals(pebbles.size(), 1)
        assertEquals(pebbles.peek(), BigInteger.valueOf(-75))
    }

    @Test
    fun divide_isCorrect() {
        val pebbles = HexStack()
        pebbles.push(BigInteger.valueOf(8))
        pebbles.push(BigInteger.valueOf(5))
        pebbles.divide()
        assertEquals(pebbles.size(), 1)
        assertEquals(pebbles.pop(), BigInteger.valueOf(1))
        pebbles.push(BigInteger.valueOf(400))
        pebbles.push(BigInteger.valueOf(20))
        pebbles.divide()
        assertEquals(pebbles.size(), 1)
        assertEquals(pebbles.peek(), BigInteger.valueOf(20))
    }

    @Test
    fun mod_isCorrect() {
        val bamBam = HexStack()
        bamBam.push(BigInteger.valueOf(43))
        bamBam.push(BigInteger.valueOf(28))
        bamBam.mod()
        assertEquals(bamBam.pop(), BigInteger.valueOf(15))
        bamBam.push(BigInteger("12341435134312", 10))
        bamBam.push(BigInteger("1324322342", 10))
        bamBam.mod()
        assertEquals(BigInteger.valueOf(75229214), bamBam.pop())
    }
}
