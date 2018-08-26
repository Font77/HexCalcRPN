package com.alloydflanagan.hexcalcrpn.model

import java.io.Serializable
import java.math.BigInteger

/**
 * An immutable version of [HexStack]. All modifying operations are changed to
 * return a new [ImmutableHexStack] with the modifications.
 */
@Suppress("UNUSED")
class ImmutableHexStack(numElements: Int = 16): ReadStack<BigInteger>, Serializable {

    private var stack = HexStack(numElements)

    /**
     * Constructs an [ImmutableHexStack]containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into the stack
     */
    constructor(c: Collection<Long>) : this(c.size) {
        for (i in c) { stack.push(i) }
    }

    constructor(hs: HexStack): this() {
        stack = HexStack(hs)
    }

    override val isEmpty get() = stack.isEmpty

    override fun getBits() = stack.getBits()

    fun setBits(value: BitsMode): ImmutableHexStack {
        val hs = HexStack(stack)
        hs.setBits(value)
        return ImmutableHexStack(hs)
    }

    /**
     * @return String with each element as a hex number, separated by newlines.
     *
     * TOS will be at the bottom, stack grows up.
     */
    override fun toString() = stack.toString()

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
    fun multiply(): ImmutableHexStack {
        val newStack = HexStack(this.stack)
        with(newStack) {
            push(pop().multiply(pop()))
        }
        return ImmutableHexStack(newStack)
    }

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
    fun divide(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            val b = pop()
            val a = pop()
            push(a.divide(b))
        }
        return ImmutableHexStack(hs)
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
    fun subtract(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            push(pop().subtract(pop()).negate())
        }
        return ImmutableHexStack(hs)
    }

    /**
     * Replaces top two items on stack with the sum.
     *
     * push(BB)
     * push(AA)
     * add()
     * pop() -> 165
     */
    fun add(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            push(pop().add(pop()))
        }
        return ImmutableHexStack(hs)
    }

    fun invert(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            push(pop().not())
        }
        return ImmutableHexStack(hs)
    }

    fun or(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            push(pop().or(pop()))
        }
        return ImmutableHexStack(hs)
    }

    fun and(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            push(pop().and(pop()))
        }
        return ImmutableHexStack(hs)
    }

    /**
     * Pops the top two elements off the stack, then pushes the result of XORing the two.
     */
    fun xor(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            val b = pop()
            val a = pop()
            push(a.xor(b))
        }
        return ImmutableHexStack(hs)
    }

    /**
     * Pops the divisor, then the dividend, off the stack, and pushes the value of
     * dividend mod divisor.
     */
    fun mod(): ImmutableHexStack {
        val hs = HexStack(this.stack)
        with (hs) {
            val b = pop()
            val a = pop()
            push(a.mod(b))
        }
        return ImmutableHexStack(hs)
    }

    override fun peek(): BigInteger? = stack.peek()

    private fun push(value: BigInteger): ImmutableHexStack {
        val hs = HexStack(this.stack)
        hs.push(value)
        return  ImmutableHexStack(hs)
    }

    private fun push(aNum: Long): ImmutableHexStack {
        val hs = HexStack(this.stack)
        hs.push(BigInteger.valueOf(aNum))
        return ImmutableHexStack(hs)
    }

    /** @throws NoSuchElementException - if queue is empty */
    fun pop(): Pair<BigInteger, ImmutableHexStack> {
        val hs = HexStack(stack)
        val value = hs.pop()
        return Pair(value, ImmutableHexStack(hs))
    }

    fun clear(): ImmutableHexStack {
        val hs = HexStack()
        hs.setBits(stack.getBits())
        return ImmutableHexStack(hs)
    }

    override operator fun contains(o: BigInteger) = stack.contains(o)

    operator fun contains(o: Long) = stack.contains(BigInteger.valueOf(o))

    override operator fun contains(o: Int) = stack.contains(BigInteger.valueOf(o.toLong()))

    override val size
            get() = stack.size

    // safe because BigInteger is immutable class
    override val contents
            get() = stack.contents

}
