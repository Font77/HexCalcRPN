package com.alloydflanagan.hexcalcrpn

import java.io.Serializable
import java.math.BigInteger
import java.util.ArrayDeque
import java.util.Deque

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
     * iterator.
     *
     * @param c the collection whose elements are to be placed into the stack
     */
    constructor(c: Collection<Long>) : this(c.size) {
        for (num in c) stack.addLast(BigInteger.valueOf(num))
        // below more elegant, may be less efficient:
        // stack.addAll(c.map { BigInteger.valueOf(it) })
    }

    /**
     * Copy constructor. We do not implement Cloneable.
     */
    constructor(hs: HexStack) : this(hs.size()) {
        stack.addAll(hs.stack)
    }

    val isEmpty get() = stack.isEmpty()

    /**
     * @return String with each element as a hex number, separated by newlines.
     */
    override fun toString() = stack.joinToString("\n").toUpperCase()

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

    fun peek(): BigInteger? = stack.peek()

    fun push(bigInteger: BigInteger) = stack.push(bigInteger)

    fun push(aNum: Long) = stack.push(BigInteger.valueOf(aNum))

    /** @throws NoSuchElementException - if queue is empty */
    fun pop(): BigInteger = stack.pop()

    fun clear() = stack.clear()

    operator fun contains(o: BigInteger) = stack.contains(o)

    operator fun contains(o: Long) = stack.contains(BigInteger.valueOf(o))

    operator fun contains(o: Int) = stack.contains(BigInteger.valueOf(o.toLong()))

    fun size() = stack.size
}
