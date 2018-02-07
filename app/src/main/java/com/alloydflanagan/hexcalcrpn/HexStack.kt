package com.alloydflanagan.hexcalcrpn

import java.io.Serializable
import java.math.BigInteger
import java.util.ArrayDeque
import java.util.Deque
import java.util.Stack

/**
 * A class to manage a stack of hexadecimal values.
 *
 * Created by aflanagan on 1/18/18.
 */

class HexStack(numElements: Int = 16) : Serializable {

    // don't inherit from Deque it's got a bunch of methods I don't want.
    private var stack: Deque<BigInteger> = ArrayDeque<BigInteger>(numElements)

    /**
     * Constructs a HexStack containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.  Note that we actually store the result of `toLong()`
     * on each item.
     *
     * @param c the collection whose elements are to be placed into the stack
     */
    constructor(c: Collection<out Number>) : this(c.size) {
        for (num in c) stack.push(BigInteger.valueOf(num.toLong()))
    }

    /**
     * Copy constructor. We do not implement Cloneable.
     */
    constructor(hs: HexStack) : this(hs.size()) {
        for (num in hs.stack) stack.push(num)
    }

    val isEmpty get() = stack.isEmpty()

    /**
     * @return String with each element as a hex number, separated by newlines.
     */
    override fun toString() = stack.joinToString("\n").toUpperCase();

    /**
     * Pops the top two elements off the stack, and pushes the result of multiplying them.
     *
     * ```
     * push(AA)
     * push(BB)
     * multiply()
     * pop() -> 7C2E
     * ```
     */
    fun multiply() = push(pop().multiply(pop()))

    /**
     * Pops the divisor, then the dividend, and pushes the value of dividend / divisor.
     *
     * ```
     * push(AA)
     * push(B)
     * divide()
     * pop() --> F
     * ```
     */
    fun divide() {
        val b = pop()
        val a = pop()
        push(a.divide(b))
    }

    /**
     * Replaces top two elements on the stack with the result of subtracting them.
     *
     * ```
     * push(BB)
     * push(AA)
     * subtract()
     * pop() -> 11
     * ```
     */
    fun subtract() = push(pop().subtract(pop()).negate())

    /**
     * Replaces top two items on stack with the sum.
     *
     * push(BB)
     * push(AA)
     * add()
     * pop() -> 165
     */
    fun add() = push(pop().add(pop()))

    fun invert() = push(pop().not())

    fun or() = push(pop().or(pop()))

    fun and() = push(pop().and(pop()))

    /**
     * Pops the top two elements off the stack, then pushes the result of XORing the two.
     */
    fun xor() {
        val b = pop()
        val a = pop()
        push(a.xor(b))
    }

    /**
     * Pops the divisor, then the dividend, off the stack, and pushes the value of
     * dividend mod divisor.
     */
    fun mod() {
        val b = pop()
        val a = pop()
        push(a.mod(b))
    }

    fun peek() = stack.peek()

    fun push(bigInteger: BigInteger) = stack.push(bigInteger)

    fun pop() = stack.pop()

    fun clear() = stack.clear()

    operator fun contains(o: Any) = stack.contains(o)

    fun size() = stack.size
}
