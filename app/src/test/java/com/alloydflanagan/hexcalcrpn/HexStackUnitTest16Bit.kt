package com.alloydflanagan.hexcalcrpn

import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.model.HexStack
import java.math.BigInteger
import kotlin.test.*

/**
 * Test the utility class HexStack.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HexStackUnitTest16Bit {

    private lateinit var stack: HexStack

    private fun assertEquals(expected: Long, actual: BigInteger?, msg: String? = null) {
        assertEquals(BigInteger.valueOf(expected), actual, msg)
    }

    @BeforeTest
    fun setup() {
        stack = HexStack(BitsMode.SIXTEEN)
    }

    @Test
    fun collectionConstructor_isCorrect() {
        val list = arrayListOf(10L, 0xACBEEFL,
                37L)
        val hs = HexStack(list, BitsMode.SIXTEEN)
        assertEquals(BigInteger.TEN, hs.pop())
        assertEquals(0xBEEF, hs.pop())
        assertEquals(37, hs.pop())
        assertEquals(0, hs.size)
    }

    @Test
    fun copyConstructor_isCorrect() {
        stack.push(0xAA15)
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
    fun addition_isCorrect() {
        stack.push(0xFFFF)
        stack.push(1)
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(0, stack.pop())
        stack.push(0xBE00)
        stack.push(0xEF)
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(0xBEEF, stack.pop())
        stack.push(0xFFFF)
        stack.push(0xBEF0) // 2's comp of 0x4110 for 16-bit word
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(0xBEEF, stack.pop())
    }

    @Test
    fun subtraction_isCorrect() {
        stack.push(7)
        stack.push(3)
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(4, stack.pop())
        stack.push(4)
        stack.push(23)
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(0x1_0000 - 19, stack.pop())

        stack.push(0xFFFFFF)
        stack.push(0x4110)
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(0xBEEF, stack.pop())
    }

    @Test
    fun multiply_isCorrect() {
        stack.push(3)
        stack.push(5)
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(15, stack.peek())
        stack.push(-5)
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(0x10000 - 75, stack.pop())
        stack.push(0x1234)
        stack.push(0xFFF)
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(0x2DCC, stack.pop())
    }

    @Test
    fun divide_isCorrect() {
        stack.push(8)
        stack.push(5)
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(1, stack.pop())
        stack.push(0xE699)
        stack.push(0xF7)
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(0xEF, stack.pop())
        stack.push(0xAAE699)
        stack.push(0xF7)
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(0xEF, stack.pop())
    }

    @Test
    fun mod_isCorrect() {
        stack.push(43)
        stack.push(28)
        stack.mod()
        assertEquals(15, stack.pop())
        stack.push(0xE699)
        stack.push(0xF6)
        stack.mod()
        assertEquals(0xEF, stack.pop())
    }

    @Test
    fun toString_isCorrect() {
        stack.push(15)
        stack.push(7)
        stack.push(23)
        assertEquals("F\n7\n17", stack.toString())
        // verify stack not damaged
        assertEquals(23, stack.pop())
        assertEquals(7, stack.pop())
        assertEquals(15, stack.pop())
        assertEquals(0, stack.size)
        assertEquals("", stack.toString())

        stack.push(0xFACE)
        stack.push(0xBEEF)
        assertEquals("FACE\nBEEF", stack.toString())
        stack.pop()
        stack.pop()
        stack.push(0xBADFACE)
        stack.push(0xBADBEEF)
        assertEquals("FACE\nBEEF", stack.toString())
    }

    @Test
    fun contains_isCorrect() {
        stack.push(0xAB15)
        stack.push(7)
        stack.push(23)
        assert(stack.contains(0xAB15))
        assert(stack.contains(0xAB15L))
        assert(stack.contains(BigInteger.valueOf(0xAB15L)))
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
        stack.push(7)
        assertEquals(7, stack.pop())
        assertFailsWith(NoSuchElementException::class) { stack.pop() }
    }

    @Test
    fun twosComplementIsCorrect() {
        stack.push(7)
        stack.twosComplement()
        var actual = stack.pop()
        assertEquals(0xFFF9, actual)

        stack.push(7)
        stack.push(0xFFF9)
        stack.add()
        actual = stack.pop()
        assertEquals(0, actual)

        stack.push(0xFFF9)
        stack.twosComplement()
        actual = stack.pop()
        assertEquals(7, actual)

        stack.push(0)  // edge case :)
        stack.twosComplement()
        actual = stack.pop()
        assertEquals(0, actual)
    }
}
