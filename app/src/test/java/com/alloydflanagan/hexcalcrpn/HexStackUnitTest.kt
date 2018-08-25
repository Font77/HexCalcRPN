package com.alloydflanagan.hexcalcrpn

import com.alloydflanagan.hexcalcrpn.model.HexStack
import java.math.BigInteger

import kotlin.test.*

/**
 * Test the utility class HexStack.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HexStackUnitTest {

    @Test
    fun constructFromCollection() {
        val list = arrayListOf(10L, 0xBEEFL,
                37L)
        val hs = HexStack(list)
        assertEquals(BigInteger.TEN, hs.pop())
        assertEquals(BigInteger.valueOf(0xBEEFL), hs.pop())
        assertEquals(BigInteger.valueOf(37L), hs.pop())
        assertEquals(0, hs.size)
    }

    @Test
    fun addition_isCorrect() {
        val fred = HexStack()
        fred.push(BigInteger.valueOf(7))
        fred.push(BigInteger.valueOf(3))
        fred.add()
        assertEquals(fred.size, 1)
        assertEquals(fred.pop(), BigInteger.valueOf(10))
    }

    @Test
    fun subtraction_isCorrect() {
        val wilma = HexStack()
        wilma.push(BigInteger.valueOf(7))
        wilma.push(BigInteger.valueOf(3))
        wilma.subtract()
        assertEquals(wilma.size, 1)
        assertEquals(wilma.pop(), BigInteger.valueOf(4))
    }

    @Test
    fun multiply_isCorrect() {
        val pebbles = HexStack()
        pebbles.push(BigInteger.valueOf(3))
        pebbles.push(BigInteger.valueOf(5))
        pebbles.multiply()
        assertEquals(pebbles.size, 1)
        assertEquals(pebbles.peek(), BigInteger.valueOf(15))
        pebbles.push(BigInteger.valueOf(-5))
        pebbles.multiply()
        assertEquals(pebbles.size, 1)
        assertEquals(pebbles.peek(), BigInteger.valueOf(-75))
    }

    @Test
    fun divide_isCorrect() {
        val pebbles = HexStack()
        pebbles.push(BigInteger.valueOf(8))
        pebbles.push(BigInteger.valueOf(5))
        pebbles.divide()
        assertEquals(1, pebbles.size)
        assertEquals(BigInteger.valueOf(1), pebbles.pop())
        pebbles.push(BigInteger.valueOf(400))
        pebbles.push(BigInteger.valueOf(20))
        pebbles.divide()
        assertEquals(1, pebbles.size)
        assertEquals(BigInteger.valueOf(20), pebbles.peek())
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

    @Test
    fun copyConstructor_isCorrect() {
        val original = HexStack()
        original.push(15)
        original.push(7)
        original.push(23)
        val copy = HexStack(original)

        while (!original.isEmpty) {
            val a = original.pop()
            val b = copy.pop()
            assertEquals(a, b)
        }
    }

    @Test
    fun collectionConstructor_isCorrect() {
        val original = ArrayList<Long>()
        original.add(15)
        original.add(7)
        original.add(23)
        val copy = HexStack(original)
        for (i in original.indices) {
            val a = copy.pop()
            assertEquals(original[i], a.toLong())
        }
    }

    @Test
    fun toString_isCorrect() {
        val original = HexStack()
        original.push(15)
        original.push(7)
        original.push(23)
        assertEquals("F\n7\n17", original.toString())
        // verify stack not damaged
        assertEquals(BigInteger.valueOf(23), original.pop())
        assertEquals(BigInteger.valueOf(7), original.pop())
        assertEquals(BigInteger.valueOf(15), original.pop())
        assertEquals(0, original.size)
        assertEquals("", original.toString())

        original.push(0xFACE)
        original.push(0xBEEF)
        assertEquals("FACE\nBEEF", original.toString())
    }

    @Test
    fun contains_isCorrect() {
        val hs = HexStack()
        hs.push(15)
        hs.push(7)
        hs.push(23)
        assert(hs.contains(15))
        assert(hs.contains(15L))
        assert(hs.contains(BigInteger.valueOf(15L)))
        assertFalse { hs.contains(12) }
        assertFalse { hs.contains(12L) }
        assertFalse { hs.contains(BigInteger.valueOf(12L)) }
    }

    @Test
    fun in_isCorrect() {
        val hs = HexStack()
        hs.push(15)
        hs.push(7)
        hs.push(23)
        assert(15 in hs)
        assert(15L in hs)
        assert(BigInteger.valueOf(15L) in hs)
        assertFalse { 12 in hs }
        assertFalse { 12L in hs }
        assertFalse { BigInteger.valueOf(12L) in hs }
    }

    @Test
    fun pop_throwsOnUnderflow() {
        val fred = HexStack(arrayListOf(7L))
        assertEquals(BigInteger.valueOf(7L), fred.pop())
        assertFailsWith(NoSuchElementException::class, {fred.pop()})
    }
}
