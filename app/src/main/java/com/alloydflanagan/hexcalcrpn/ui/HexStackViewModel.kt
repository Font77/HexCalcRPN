package com.alloydflanagan.hexcalcrpn.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.model.HexStack
import com.alloydflanagan.hexcalcrpn.model.ReadStack
import java.math.BigInteger

/**
 * A ViewModel for a stack-based calculator.
 *
 * This class implements the operations taken in response to user key presses.
 *
 * The model is an instance of HexStack + a BigInteger for the currently entered value.
 */
class HexStackViewModel: AbstractStackViewModel<BigInteger>() {

    /**
     * we maintain a value as a [StringBuilder] privately so we can just stick digits on to the end.
     */
    private var mCurrStr = StringBuilder()

    /**
     * Implementation for `stack` value.
     */
    private val mStack = MutableLiveData<HexStack>()

    /**
     * private mutable [BigInteger] value being entered by user. We expose it read-only in
     * [HexStackViewModel.getCurrent].
     */
    private val mCurrent = MutableLiveData<BigInteger>()


    init {
        mStack.postValue(HexStack())
        mCurrent.postValue(BigInteger.valueOf(0))
    }

    /**
     * Returns the value currently being entered by the user. Note that if there is no value
     * (i.e. at start or after an operator) this will have a value of [BigInteger.ZERO]
     * anyway. Use [HexStackViewModel.hasCurrentValue] to distinguish this case.
     */
    override fun getCurrent(): LiveData<BigInteger> {
        return mCurrent
    }

    // helper for frequent conversion to BigInteger
    private fun currentBigInt() = if (mCurrStr.isNotEmpty()) mCurrStr.toString().toBigInteger(16) else BigInteger.ZERO

    /**
     * True if there is a valid current value. Note the value of [HexStackViewModel.getCurrent]
     * will be 0 even when this is false.
     */
    override fun hasCurrentValue() = mCurrStr.isNotEmpty()

    /**
     * Clear any value entered by the user. After this, [HexStackViewModel.getCurrent] will
     * return [BigInteger.ZERO] and [HexStackViewModel.hasCurrentValue] will return false.
     */
    private fun clearCurrentValue() {
        mCurrStr = StringBuilder()
        mCurrent.postValue(BigInteger.ZERO)
    }

    /**
     * a stack of [BigInteger]s, observable.
     */
    @Suppress("UNCHECKED_CAST")  // because type erasure, of course.
    override fun getStack(): LiveData<ReadStack<BigInteger>> =
            mStack as LiveData<ReadStack<BigInteger>>

    /**
     * Execute an operator. If the user has typed in a value but not pushed it, push the current
     * value first.
     */
    private fun doBinaryOp(op: Char) {
        if (hasCurrentValue()) {
            mStack.value?.push(currentBigInt())
            clearCurrentValue()
        }
        when (op) {
            '+' -> mStack.value?.add()
            '-' -> mStack.value?.subtract()
            '*' -> mStack.value?.multiply()
            '/' -> mStack.value?.divide()
            '%' -> mStack.value?.mod()
            'X' -> mStack.value?.xor()
            '&' -> mStack.value?.and()
            '|' -> mStack.value?.or()
            else -> throw IllegalStateException("op must be an operator: got '$op'")
        }
        mStack.postValue(mStack.value)
    }

    /**
     * If there is a current value, a unary op acts on it. If not, it acts on TOS.
     */
    private fun doUnaryOp(op: Char) {
        val hs = mStack.value
        if (hs != null) {
            // currently all unary ops don't work in infinite mode, so
            if (hs.bits == BitsMode.INFINITE) return
            // if stack is empty, operate on current value even if it is "0"
            if (hasCurrentValue() || hs.isEmpty) {
                hs.push(currentBigInt())
                when (op) {
                    '~' -> hs.invert()
                    '2' -> hs.twosComplement()
                    else -> throw IllegalStateException("op must be unary operator: got '$op'")
                }
                val temp = hs.pop()
                mCurrStr = StringBuilder()
                mCurrStr.append(temp.toString(16).toUpperCase())
                mCurrent.postValue(temp)
                // Note mStack is unchanged!
            } else {
                // hs.isEmpty == false
                when (op) {
                    '~' -> {
                        hs.invert()
                        mStack.postValue(hs)
                    }
                    '2' -> {
                        hs.twosComplement()
                        mStack.postValue(hs)
                    }
                    else -> throw IllegalStateException("op must be unary operator: got '$op'")
                }
            }
        }
    }

    override fun handleInput(input: Char) {
        when (input) {
            in '0'..'9'-> {
                mCurrStr.append(input)
                mCurrent.postValue(currentBigInt())
            }
            in 'A'..'F' -> {
                mCurrStr.append(input)
                mCurrent.postValue(currentBigInt())
            }
            '=' -> {
                mStack.value?.push(currentBigInt())
                // weird, but must trigger onChange()
                mStack.postValue(mStack.value)
                clearCurrentValue()
            }
            else -> throw IllegalStateException("I don't know how to handle input '$input'")
        }
    }

    override fun handleOperator(op: Char) {
        when (op) {
            // if user is entering value, 'c' clears it. if not removes one value from stack
            'c' -> {
                if (hasCurrentValue())
                    clearCurrentValue()
                else if (mStack.value?.size ?: 0 > 0){
                    mStack.value?.pop()
                    mStack.postValue(mStack.value)
                }
            }
            in BINARY_OPS -> {
                // do we have enough values?
                var count = mStack.value?.size ?: 0
                if (mCurrStr.isNotEmpty()) count += 1  // because current will be added to stack
                if (count >= 2)
                    doBinaryOp(op)
            }
            in UNARY_OPS -> {
                var count = mStack.value?.size ?: 0
                if (mCurrStr.isNotEmpty()) count += 1  // because current will be added to stack
                if (count >= 1)
                    doUnaryOp(op)
            }
            else -> throw IllegalStateException("I don't know how to handle operator '$op'")
        }
    }

    override fun handleModeInput(input: BitsMode) {
        val stack = mStack.value
        if (stack != null) {
            stack.bits = input
            mStack.postValue(stack)
        }
    }

    companion object {
        val BINARY_OPS = HashSet<Char>(arrayListOf('+', '-', '*', '/', '%', 'X', '&', '|'))
        val UNARY_OPS = HashSet<Char>(arrayListOf('!', '~', '2'))  // 2 => 2's complement
    }
}
