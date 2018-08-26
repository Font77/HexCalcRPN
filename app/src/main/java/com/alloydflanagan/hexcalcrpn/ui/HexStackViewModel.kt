package com.alloydflanagan.hexcalcrpn.ui

import androidx.lifecycle.*
import com.alloydflanagan.hexcalcrpn.model.HexStack
import com.alloydflanagan.hexcalcrpn.model.ReadStack
import java.math.BigInteger

class HexStackViewModel: AbstractStackViewModel<BigInteger>() {

    /**
     * we maintain a value as a [StringBuilder] privately so we can just stick digits on to the end.
     */
    private var _currStr = StringBuilder()

    /**
     * Implementation for `stack` value.
     */
    private val _stack = MutableLiveData<HexStack>()

    /**
     * private mutable [BigInteger] value being entered by user. We expose it read-only in
     * [HexStackViewModel.getCurrent].
     */
    val _current = MutableLiveData<BigInteger>()

    init {
        _stack.postValue(HexStack())
    }

    /**
     * Returns the value currently being entered by the user. Note that if there is no value
     * (i.e. at start or after an operator) this will have a value of [BigInteger.ZERO]
     * anyway. Use [HexStackViewModel.hasCurrentValue] to distinguish this case.
     */
    override fun getCurrent(): LiveData<BigInteger> {
        return _current
    }

    // helper for frequent conversion to BigInteger
    private fun currentBigInt() = _currStr.toString().toBigInteger(16)

    /**
     * [true] if there is a valid current value. Note the value of [HexStackViewModel.getCurrent]
     * will be 0 even when this is [false]
     */
    override fun hasCurrentValue() = _currStr.isNotEmpty()

    /**
     * Clear any value entered by the user. After this, [HexStackViewModel.getCurrent] will
     * return [BigInteger.ZERO] and [HexStackViewModel.hasCurrentValue] will return [false]
     */
    private fun clearCurrentValue() {
        _currStr = StringBuilder()
        _current.postValue(BigInteger.ZERO)
    }

    /**
     * a stack of [BigIntegers], observable.
     */
    @Suppress("UNCHECKED_CAST")  // because type erasure, of course.
    override fun getStack(): LiveData<ReadStack<BigInteger>> =
            _stack as LiveData<ReadStack<BigInteger>>

    /**
     * Execute an operator. If the user has typed in a value but not pushed it, push the current
     * value first.
     */
    private fun doBinaryOp(op: Char) {
        if (hasCurrentValue()) {
            _stack.value?.push(currentBigInt())
            clearCurrentValue()
        }
        when (op) {
            '+' -> _stack.value?.add()
            '-' -> _stack.value?.subtract()
            '*' -> _stack.value?.multiply()
            '/' -> _stack.value?.divide()
            '%' -> _stack.value?.mod()
            'X' -> _stack.value?.xor()
            '&' -> _stack.value?.and()
            '|' -> _stack.value?.or()
            else -> throw IllegalStateException("op must be an operator: got '$op'")
        }
        _stack.postValue(_stack.value)
    }

    /**
     * If there is a current value, a unary op acts on it. If not, it acts on TOS.
     */
    private fun doUnaryOp(op: Char) {
        val hs = _stack.value
        if (hs != null) {
            if (hasCurrentValue()) {
                hs.push(currentBigInt())
                when (op) {
                    '~' -> hs.invert()
                    else -> throw IllegalStateException("op must be unary operator: got '$op'")
                }
                val temp = hs.pop()
                _currStr = StringBuilder()
                _currStr.append(temp.toString(16).toUpperCase())
                _current.postValue(temp)
            } else {
                if (hs.size > 0) {
                    when (op) {
                        '~' -> {
                            hs.invert()
                            _stack.postValue(hs)
                        }
                        else -> throw IllegalStateException("op must be unary operator: got '$op'")
                    }
                }
            }
        }
    }

    override fun handleInput(input: Char) {
        when (input) {
            in '0'..'9'-> {
                _currStr.append(input)
                _current.postValue(currentBigInt())
            }
            in 'A'..'F' -> {
                _currStr.append(input)
                _current.postValue(currentBigInt())
            }
            '=' -> {
                _stack.value?.push(currentBigInt())
                // weird, but must trigger onChange()
                _stack.postValue(_stack.value)
                clearCurrentValue()
            }
            in BINARY_OPS -> {
                // do we have enough values?
                var count = _stack.value?.size ?: 0
                if (_currStr.length > 0) count += 1  // because current will be added to stack
                if (count >= 2)
                    doBinaryOp(input)
            }
            in UNARY_OPS -> {
                var count = _stack.value?.size ?: 0
                if (_currStr.length > 0) count += 1  // because current will be added to stack
                if (count >= 1)
                    doUnaryOp(input)
            }
            // if user is entering value, 'c' clears it. if not removes one value from stack
            'c' -> {
                if (hasCurrentValue())
                    clearCurrentValue()
                else if (_stack.value?.size ?: 0 > 0){
                    _stack.value?.pop()
                    _stack.postValue(_stack.value)
                }
            }
            else -> throw IllegalStateException("I don't know how to handle input '$input'")
        }
    }

    companion object {
        val BINARY_OPS = HashSet<Char>(arrayListOf('+', '-', '*', '/', '%', 'X', '&', '|'))
        val UNARY_OPS = HashSet<Char>(arrayListOf('!', '~'))
    }
}
