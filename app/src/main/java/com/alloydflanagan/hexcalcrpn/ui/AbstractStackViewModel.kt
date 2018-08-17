package com.alloydflanagan.hexcalcrpn.ui

import androidx.lifecycle.*
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
     * The _current value being edited. Note there may be no such value, in which case we return [null]
     */
    abstract fun getCurrent(): LiveData<T>

    /**
     * Process a character of input, either:
     * - a hex digit
     * - an operator
     * - '=' --> push _current value
     */
    abstract fun handleInput(input: Char)

    abstract fun hasCurrentValue(): Boolean
}
