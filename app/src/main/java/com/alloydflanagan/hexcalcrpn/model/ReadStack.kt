package com.alloydflanagan.hexcalcrpn.model

enum class BitsMode(private val numBits: Int) {
    EIGHT(8), SIXTEEN(16), THIRTY_TWO(32), SIXTY_FOUR(64), INFINITE(0);

    override fun toString(): String {
        return if (numBits == 0) "\u221E" else numBits.toString()
    }
}

/**
 * An interface to a stack of values. This implements only read operations.
 *
 * A [ReadStack] cannot change its contents (as long as [ReadStack.contents] is correctly
 * implemented).
 */

interface ReadStack<T> {
    val isEmpty: Boolean

    var bits: BitsMode

    /**
     * Returns the _current top of the stack, without altering the stack.
     */
    fun peek(): T?

    /**
     * True if the stack contains `o`.
     */
    operator fun contains(o: T): Boolean

    /**
     * True if the stack contains the value of o converted to type [T].
     *
     * Always false if no such conversion is possible.
     */
    operator fun contains(o: Int): Boolean

    /**
     * Number of elements on the stack.
     */
    val size: Int

    /**
     * A list of the contents of the stack, such that index 0 is top of stack.
     * Elements are copies, or references to immutable objects.
     */
    val contents: List<T>
}
