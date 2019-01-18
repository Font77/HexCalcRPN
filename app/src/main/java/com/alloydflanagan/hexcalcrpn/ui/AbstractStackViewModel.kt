package com.alloydflanagan.hexcalcrpn.ui

import androidx.lifecycle.*
import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.model.ReadStack

/**
 * A [ViewModel] with a set of operations to process input and provide a _current value and a stack of hex values.
 */
abstract class AbstractStackViewModel<T>: ViewModel() {
    /**
     * A read-only view of the underlying stack of numbers of type T
     */
    abstract fun getStack(): LiveData<ReadStack<T>>

    /**
     * The _current value being edited. Note there may be no such value, in which case we return <code>null</code>.
     */
    abstract fun getCurrent(): LiveData<T>

    /**
     * Process a character of input, either:
     * - a hex digit
     * - '=' --> push _current value
     */
    abstract fun handleInput(input: Char)

    /**
     * Process an input operator
     */
    abstract fun handleOperator(op: Char)

    /**
     * Does the stack have a current value input?
     */
    @Suppress("unused")
    abstract fun hasCurrentValue(): Boolean

    /**
     * Process input that causes a mode change --> word size, (un)signed,
     * etc.
     */
    abstract fun handleModeInput(input: BitsMode)
}
