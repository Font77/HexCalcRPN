package com.alloydflanagan.hexcalcrpn.model

import java.io.Serializable
import java.math.BigInteger
import java.util.*


/**
 * A class to manage a stack of [BigInteger]s with operators.
 */
@Suppress("unused")
class HexStack(numElements: Int = 16): ReadStack<BigInteger>, Serializable {

    // Underlying stack. We don't inherit from [Deque] because we haven't implemented all the methods
    private var stack: Deque<BigInteger> = ArrayDeque<BigInteger>(numElements)

   /**
     * The emulated word size for calculations.
     */
    override var bits = BitsMode.INFINITE
            /**
             * Set number of bits assumed for calculations. NOTE: resets the
             * stack to empty.
             */
            set(value) {
                // decided not to check if _bits == value; always clear stack for consistency.
                field = value
                stack.clear()
            }

    override val size
        get() = stack.size

    /**
     * Returns a copy of the current stack of [BigInteger]s.
     */
    // safe because BigInteger is immutable class
    override val contents
        get() = stack.map { it }

    /**
     * Truncates the value to fit into the current bits mode.
     */
    private fun truncate(value: BigInteger): BigInteger {
        return when(bits) {
            BitsMode.INFINITE -> value
            BitsMode.EIGHT -> value.mod(MOD_8)
            BitsMode.SIXTEEN -> value.mod(MOD_16)
            BitsMode.THIRTY_TWO -> value.mod(MOD_32)
            BitsMode.SIXTY_FOUR -> value.mod(MOD_64)
        }
    }

    /**
     * Constructs a [HexStack] containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into the stack
     * @param bits Number of bits in word size
     */
    constructor(c: Collection<Long>,
                bits: BitsMode = BitsMode.INFINITE) : this(c.size) {
        this.bits = bits
        stack.addAll(c.map { truncate(BigInteger.valueOf(it)) })
    }

    /**
     * Constructs a [HexStack] with non-default [BitsMode]
     *
     * @param bits Number of bits in word size
     */
    constructor(bits: BitsMode): this() {
        this.bits = bits
    }

    /**
     * Copy constructor. We do not implement [kotlin::Cloneable].
     */
    constructor(hs: HexStack) : this(hs.size) {
        bits = hs.bits
        stack.addAll(hs.stack)
    }

    override val isEmpty get() = stack.isEmpty()

    /**
     * @return String with each element as a hex number, separated by newlines.
     *
     * TOS will be at the bottom, stack grows up.
     */
    override fun toString(): String {
        val builder = StringBuilder()
        // pop each item, convert to string, add it back to deque
        for (i in 0 until stack.size) {
            val value = stack.pop()
            val asString = value.toString(16).toUpperCase()
            if (i != 0) builder.insert(0, '\n')
            builder.insert(0, asString)
            stack.addLast(value)
        }
        return builder.toString()
    }

    /**
     * Pops the top two elements off the stack, and pushes the result of multiplying them.
     *
     * <code>
     * push(AA)
     * push(BB)
     * multiply()
     * pop() -> 7C2E
     * </code>
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
    fun subtract(){
        val b = pop()
        val a = pop()
        push(a.subtract(b))
    }

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

    fun twosComplement() {
        val a = pop()
        val b = when(bits) {
            // .mod(value) fixes result if a is <= 0
            BitsMode.EIGHT -> MOD_8.subtract(a).mod(MOD_8)
            BitsMode.SIXTEEN -> MOD_16.subtract(a).mod(MOD_16)
            BitsMode.THIRTY_TWO -> MOD_32.subtract(a).mod(MOD_32)
            BitsMode.SIXTY_FOUR -> MOD_64.subtract(a).mod(MOD_64)
            BitsMode.INFINITE -> throw Exception("2's complement is meaningless with 'infinite' word size.")
        }
        push(b)
    }

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

    @Suppress("private")
    fun push(value: BigInteger) = stack.push(truncate(value))

    fun push(aNum: Long) = stack.push(truncate(BigInteger.valueOf(aNum)))

    /** @throws NoSuchElementException - if queue is empty */
    @Suppress("private")
    fun pop(): BigInteger = stack.pop()

    override operator fun contains(o: BigInteger) = stack.contains(o)

    operator fun contains(o: Long) = stack.contains(BigInteger.valueOf(o))

    override operator fun contains(o: Int) = stack.contains(BigInteger.valueOf(o.toLong()))

    companion object {
        val MOD_8: BigInteger = BigInteger.valueOf(0x100)
        val MOD_16: BigInteger = BigInteger.valueOf(0x1_0000)
        val MOD_32: BigInteger = BigInteger.valueOf(0x1_0000_0000)
        // can't represent 64 bits as long literal
        val MOD_64: BigInteger = BigInteger.valueOf(0x1000_0000_0000_0000) * BigInteger.valueOf(0x10)
    }
}
