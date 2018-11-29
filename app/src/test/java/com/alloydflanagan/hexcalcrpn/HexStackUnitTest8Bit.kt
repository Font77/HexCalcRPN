package com.alloydflanagan.hexcalcrpn

import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.model.HexStack
import com.alloydflanagan.hexcalcrpn.model.SignModes
import java.math.BigInteger

import kotlin.test.*

/**
 * Test the utility class HexStack.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HexStackUnitTest8Bit {

    private lateinit var stack: HexStack

    @BeforeTest
    fun setup() {
        stack = HexStack(BitsMode.EIGHT)
    }

    @Test
    fun constructFromCollection() {
        val list = arrayListOf(10L, 0xBEEFL,
                37L)
        val hs = HexStack(list, BitsMode.EIGHT)
        assertEquals(BigInteger.TEN, hs.pop())
        assertEquals(BigInteger.valueOf(0xEFL), hs.pop())
        assertEquals(BigInteger.valueOf(37L), hs.pop())
        assertEquals(0, hs.size)
    }

    @Test
    fun addition_isCorrect() {
        stack.push(BigInteger.valueOf(7))
        stack.push(BigInteger.valueOf(3))
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(10), stack.pop())
        stack.push(BigInteger.valueOf(156))
        stack.push(BigInteger.valueOf(100))
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(0), stack.pop())
    }

    @Test
    fun subtraction_isCorrect() {
        stack.push(BigInteger.valueOf(7))
        stack.push(BigInteger.valueOf(3))
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(4), stack.pop())
        stack.push(BigInteger.valueOf(4))
        stack.push(BigInteger.valueOf(23))
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(256-19), stack.pop())
    }

    @Test
    fun multiply_isCorrect() {
        stack.push(BigInteger.valueOf(3))
        stack.push(BigInteger.valueOf(5))
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(15), stack.peek())
        stack.push(BigInteger.valueOf(-5))
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(256-75), stack.peek())
    }

    @Test
    fun divide_isCorrect() {
        stack.push(BigInteger.valueOf(8))
        stack.push(BigInteger.valueOf(5))
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(1), stack.pop())
        stack.push(BigInteger.valueOf(200))
        stack.push(BigInteger.valueOf(20))
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(BigInteger.valueOf(10), stack.peek())
    }

    @Test
    fun mod_isCorrect() {
        stack.push(BigInteger.valueOf(43))
        stack.push(BigInteger.valueOf(28))
        stack.mod()
        assertEquals(BigInteger.valueOf(15), stack.pop())
        stack.push(BigInteger("12341435134312", 10)) // 0x68
        stack.push(BigInteger("1324322342", 10)) // 0x26
        stack.mod()
        assertEquals(BigInteger.valueOf(0x1C), stack.pop())
    }

    @Test
    fun copyConstructor_isCorrect() {
        stack.push(15)
        stack.push(7)
        stack.push(23)
        val copy = HexStack(stack)

        assertEquals(stack.bits, copy.bits)

        while (!stack.isEmpty) {
            val expected = stack.pop()
            val actual = copy.pop()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun collectionConstructor_isCorrect() {
        val original = arrayListOf(15L, 7L, 23L)
        val copy = HexStack(original, BitsMode.EIGHT, SignModes.UNSIGNED)
        for (i in original.indices) {
            val a = copy.pop()
            assertEquals(original[i], a.toLong())
        }
    }

    @Test
    fun toString_isCorrect() {
        stack.push(15)
        stack.push(7)
        stack.push(23)
        assertEquals("F\n7\n17", stack.toString())
        // verify stack not damaged
        assertEquals(BigInteger.valueOf(23), stack.pop())
        assertEquals(BigInteger.valueOf(7), stack.pop())
        assertEquals(BigInteger.valueOf(15), stack.pop())
        assertEquals(0, stack.size)
        assertEquals("", stack.toString())

        stack.push(0xFACE)
        stack.push(0xBEEF)
        assertEquals("CE\nEF", stack.toString())
    }

    @Test
    fun contains_isCorrect() {
        stack.push(15)
        stack.push(7)
        stack.push(23)
        assert(stack.contains(15))
        assert(stack.contains(15L))
        assert(stack.contains(BigInteger.valueOf(15L)))
        assertFalse { stack.contains(12) }
        assertFalse { stack.contains(12L) }
        assertFalse { stack.contains(BigInteger.valueOf(12L)) }
    }

    @Test
    fun in_isCorrect() {
        stack.push(15)
        stack.push(7)
        stack.push(23)
        assert(15 in stack)
        assert(15L in stack)
        assert(BigInteger.valueOf(15L) in stack)
        assertFalse { 12 in stack }
        assertFalse { 12L in stack }
        assertFalse { BigInteger.valueOf(12L) in stack }
    }

    @Test
    fun pop_throwsOnUnderflow() {
        stack.push(7L)
        assertEquals(BigInteger.valueOf(7L), stack.pop())
        assertFailsWith(NoSuchElementException::class) { stack.pop() }
    }
}
