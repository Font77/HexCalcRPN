package com.alloydflanagan.hexcalcrpn.model

import java.io.Serializable
import java.math.BigInteger
import java.util.ArrayDeque
import java.util.Deque

/**
 * A class to manage a stack of BigIntegers with operators.

 * TODO: must provide "number of bits" setting
 *
 * (Do we need to keep record of actual numbers entered, so we can change on-the-fly without clearing
 * stack??)
 */

class HexStack(numElements: Int = 16): ReadStack<BigInteger>, Serializable {

    // we don't implement all methods of a Deque, so keep it private
    private var stack: Deque<BigInteger> = ArrayDeque<BigInteger>(numElements)

    /**
     * Constructs a HexStack containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into the stack
     */
    constructor(c: Collection<Long>) : this(c.size) {
        stack.addAll(c.map { BigInteger.valueOf(it) })
    }

    /**
     * Copy constructor. We do not implement Cloneable.
     */
    constructor(hs: HexStack) : this(hs.size) {
        stack.addAll(hs.stack)
    }

    override val isEmpty get() = stack.isEmpty()

    /**
     * @return String with each element as a hex number, separated by newlines.
     *
     * TOS will be at the bottom, stack grows up.
     */
    override fun toString(): String {
        val bldr = StringBuilder()
        // pop each item, convert to string, add it back to deque
        for (i in 0 until stack.size) {
            val value = stack.pop()
            val asString = value.toString(16).toUpperCase()
            if (i != 0) bldr.insert(0, '\n')
            bldr.insert(0, asString)
            stack.addLast(value)
        }
        return bldr.toString()
    }

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

    override fun peek(): BigInteger? = stack.peek()

    fun push(value: BigInteger) = stack.push(value)

    fun push(aNum: Long) = stack.push(BigInteger.valueOf(aNum))

    /** @throws NoSuchElementException - if queue is empty */
    fun pop(): BigInteger = stack.pop()

    fun clear() = stack.clear()

    override operator fun contains(o: BigInteger) = stack.contains(o)

    operator fun contains(o: Long) = stack.contains(BigInteger.valueOf(o))

    override operator fun contains(o: Int) = stack.contains(BigInteger.valueOf(o.toLong()))

    override val size
            get() = stack.size

    // safe because BigInteger is immutable class
    override val contents
            get() = stack.map { it }

}
