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
class HexStackUnitTest64Bit {

    private lateinit var stack: HexStack

    companion object {
        var BIG_INT_MAX_64 = BigInteger.valueOf(0xFFF_FFFF_FFFF_FFFF).multiply(BigInteger.valueOf(0x10)).plus(BigInteger.valueOf(0xF))
    }

    private fun assertEquals(expected: Long, actual: BigInteger?, msg: String? = null) {
        assertEquals(BigInteger.valueOf(expected), actual, msg)
    }

    @BeforeTest
    fun setup() {
        stack = HexStack(BitsMode.SIXTY_FOUR)
    }

    @Test
    fun collectionConstructor_isCorrect() {
        val list = arrayListOf(10, 0xBAD_BEEF,
                0xBAD_FFFF_FFFF)
        val hs = HexStack(list, BitsMode.SIXTY_FOUR)
        assertEquals(BigInteger.TEN, hs.pop())
        assertEquals(0xBAD_BEEF, hs.pop())
        assertEquals(0xBAD_FFFF_FFFF, hs.pop())
        assertEquals(0, hs.size)
    }

    @Test
    fun copyConstructor_isCorrect() {
        stack.push(BIG_INT_MAX_64)
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
        stack.push(7)
        stack.push(3)
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(10, stack.pop())
        stack.push(0xFFFF_FFFF)
        stack.push(1)
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(0x1_0000_0000, stack.pop())
        stack.push(BIG_INT_MAX_64)
        stack.push(1)
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(0, stack.pop())
        stack.push(53)
        stack.push(BIG_INT_MAX_64.minus(BigInteger.valueOf(27)))  // 2's comp of 28
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(25L, stack.pop())
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
        assertEquals(BIG_INT_MAX_64.minus(BigInteger.valueOf(18)), stack.pop()) // 2's comp of 19 in 64 bits
        stack.push(BIG_INT_MAX_64)
        stack.push(0xABCD)
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(BIG_INT_MAX_64.minus(BigInteger.valueOf(0xABCD)), stack.pop())
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
        val twosComplOf75 = BIG_INT_MAX_64 - BigInteger.valueOf(74)
        assertEquals(twosComplOf75, stack.pop())
        stack.push(0xBAD_BEEF)
        stack.push(0xFFF)
        // == BAD0413111 ==> D0413111
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(0xBA_D041_3111, stack.pop())
        val reallyBadBeef = BigInteger.valueOf(0xBADB_ADBA_DBAD)
                .multiply(BigInteger.valueOf(0x1_0000))
                .plus(BigInteger.valueOf(0xBEEF))
        stack.push(reallyBadBeef)
        stack.push(0xFFF)
        stack.multiply()
        // == BAD0000000413111 ==> 413111
        assertEquals(1, stack.size)
        assertEquals(0x41_3111, stack.pop())
    }

    @Test
    fun divide_isCorrect() {
        stack.push(8)
        stack.push(5)
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(1, stack.pop())
        stack.push(0xB_ADBA_DBAD_BEEF)
        stack.push(0xFFF)
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(0xBA_E75C_309E, stack.pop())
    }

    @Test
    fun mod_isCorrect() {
        stack.push(43)
        stack.push(28)
        stack.mod()
        assertEquals(15, stack.pop())
        stack.push(BIG_INT_MAX_64.minus(BigInteger.valueOf(0xBEEF)))
        stack.push(0x4EEF_8E26)
        stack.mod()
        assertEquals(0x26A3_0A44, stack.pop())
    }

    @Test
    fun toString_isCorrect() {
        stack.push(0x76DB_7168)
        stack.push(0xD041_3111)
        stack.push(0xFFF)
        assertEquals("76DB7168\nD0413111\nFFF", stack.toString())
        // verify stack not damaged
        assertEquals(0xFFF, stack.pop())
        assertEquals(0xD041_3111, stack.pop())
        assertEquals(0x76DB_7168, stack.pop())
        assertEquals(0, stack.size)
        assertEquals("", stack.toString())

        stack.push(0x1234_FACE_0FF1)
        stack.push(0x5678_BEEF_FACE)
        assertEquals("1234FACE0FF1\n5678BEEFFACE", stack.toString())
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
        stack.push(7)
        assertEquals(7, stack.pop())
        assertFailsWith(NoSuchElementException::class) { stack.pop() }
    }
}
