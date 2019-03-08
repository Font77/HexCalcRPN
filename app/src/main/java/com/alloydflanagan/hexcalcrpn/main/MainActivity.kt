//    HexCalcRPN - Android application to do arithmetic on hexadecimal values
//    Copyright (C) 2019 Adrian L Flanagan
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//            along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.alloydflanagan.hexcalcrpn.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.*
import com.alloydflanagan.hexcalcrpn.ui.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_word_size.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.math.BigInteger

/**
 * Main activity: display current word size, the calculator stack, the current entry, and the
 * keypad. Handles key clicks.
 */
class MainActivity : AppCompatActivity(),
        OnClickListener,
        OperatorFragment.OnFragmentInteractionListener,
        DigitsFragment.OnFragmentInteractionListener,
        WordSizeFragment.OnFragmentInteractionListener,
        KodeinAware {

    /** access to <a href="https://kodein.org/">Kodein</a> bindings */
    override val kodein by closestKodein()

    /**
     * The view model. Owns the stack, and handles presentation and user input.
     */
    val viewModel: AbstractStackViewModel<BigInteger> by instance()

    private lateinit var preferences: AppPreferences

    /**
     * Set up our options menu. Currently the only option is "Settings".
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    /**
     * Handle user selection of "Settings" by opening [SettingsActivity].
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Handle user change of the word size, by passing to [HexStackViewModel]. Also updates the
     * "last selected" preference setting.
     */
    override fun onWordSizeFragmentInteraction(bits: BitsMode) {
        viewModel.handleModeInput(bits)
        preferences.lastWordSize = bits.toPreference()
    }

    /**
     * Handle user input from the [DigitsFragment], i.e. the set of 16 digit keys. Just delegates
     * to [HexStackViewModel].
     */
    override fun onDigitsFragmentInteraction(digit: Char) = viewModel.handleInput(digit)

    /**
     * Handle user input from the [OperatorFragment], i.e. all they keys that _aren't_ hex digits.
     * Just delegates to [HexStackViewModel].
     */
    override fun onOperatorFragmentInteraction(operator: String) = viewModel.handleOperator(operator[0])

    /**
     * Handle clicks on buttons which are part of the main form, currently only "=>".
     */
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_equals) {
            viewModel.handleInput('=')
        }
        if (v != null) {
            Timber.d("onClick got view ID ${v.id} (${v.javaClass.name})")
        }
    }

    /**
     * Set mode selected in [WordSizeFragment] based on bit size of model.
     */
    private fun selectCurrentBitsMode() {
        val bitSize = viewModel.getStack().value?.bits
        if (bitSize != null) {
            when (bitSize) {
                BitsMode.EIGHT -> radio_8.isChecked = true
                BitsMode.SIXTEEN -> radio_16.isChecked = true
                BitsMode.THIRTY_TWO -> radio_32.isChecked = true
                BitsMode.SIXTY_FOUR -> radio_64.isChecked = true
                BitsMode.INFINITE -> radio_inf.isChecked = true
            }
        }
    }

    /**
     * Sets [HexStackViewModel] bits value from preferences. Should only be called once!
     */
    private fun setBitsModeFromPrefs() {
        val stack = viewModel.getStack().value
        if (stack != null) {
            if (preferences.prefInitSize == BitsPreference.PREVIOUS) {
                val pref = preferences.lastWordSize
                stack.bits = BitsMode.fromPreference(pref)
            } else {
                stack.bits = BitsMode.fromPreference(preferences.prefInitSize)
            }
        } else {
            throw Error("Can't get HexStack from the ViewModel!!")
        }
    }

    /**
     * Set up preferences, form, and action bar. Register listeners for the [HexStackViewModel]'s
     * stack and current value.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(hex_app_bar)
        // brv_modes.highlightedButton = BitsMode.INFINITE.toString()

        preferences = AppPreferences(this)

        // our viewModel may have survived closing this activity and recreating
        // so make sure our output is synched
        updateCurrent(viewModel.getCurrent().value ?: BigInteger.ZERO)
        val stack = viewModel.getStack().value
        if (stack != null) updateStack(stack) else tv_output.text = ""

        viewModel.getCurrent().observe(this, Observer {
            if (it != null) {  // probably unnecessary
                updateCurrent(it)
            }
        })

        viewModel.getStack().observe(this, Observer {
            updateStack(it)
        })
    }

    private fun updateStack(newValue: ReadStack<BigInteger>) {
        val txt = if (preferences.prefDigitSep) {
            newValue.toSpacedString().toUpperCase()
        } else {
            newValue.toString().toUpperCase()
        }

        if (txt != tv_output.text) tv_output.text = txt
        selectCurrentBitsMode()
    }

    private fun updateCurrent(newValue: BigInteger) {
        val fred = if (preferences.prefDigitSep) {
            newValue.toSpacedString(16).toUpperCase()
        }
        else {
            newValue.toString(16).toUpperCase()
        }

        if (fred != tv_current.text) tv_current.text = fred
    }

    /**
     * Need to do some setup after window attached because we need the
     * [androidx.lifecycle.MutableLiveData] instances
     * set up.
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Timber.d("attached to window")
        if (viewModel.getStack().value == null) {
            throw Error("viewModel should have a stack but doesn't")
        }
        setBitsModeFromPrefs()
        selectCurrentBitsMode()
    }
}
