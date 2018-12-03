package com.alloydflanagan.hexcalcrpn.main

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.model.ReadStack
import com.alloydflanagan.hexcalcrpn.model.SignModes
import com.alloydflanagan.hexcalcrpn.ui.AbstractStackViewModel
import com.alloydflanagan.hexcalcrpn.ui.ButtonRowView
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.math.BigInteger

/**
 * Main activity: displays output, _current entry, and keys. Handles key clicks.
 */
class MainActivity : AppCompatActivity(), OnClickListener, KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: AbstractStackViewModel<BigInteger> by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getCurrent().observe(this, Observer {
            val fred = it?.toString(16)?.toUpperCase() ?: ""
            if (fred != tv_current.text) tv_current.text = fred
        })

        viewModel.getStack().observe(this, Observer {
            val txt = it.toString().toUpperCase()
            if (txt != tv_output.text) tv_output.text = txt
            updateStatus(it)
        })
    }

    private fun updateStatus(stack: ReadStack<BigInteger>) {
        tv_status.text = getString(R.string.mode_display,
                getString(if (stack.signed == SignModes.SIGNED) R.string.signed_mode else R.string.unsigned_mode),
                getString(R.string.bits_label),
                stack.bits.toString()
        )

        // some operations don't make sense in some modes
        if (stack.bits == BitsMode.INFINITE) {
            brv_2.disableButton(5)  // '~'
            brv_modes.disableButton(5) // SIGN
        } else {
            brv_2.enableButton(5)  // '~'
            brv_modes.enableButton(5) // SIGN
        }
    }

    override fun onClick(v: View) {
        when (v) {
            is ButtonRowView ->
                if (v.clickedText != "") {  // right now clickedText == "" is how we know button is disabled
                    if (v.id == brv_modes.id)
                        viewModel.handleModeInput(v.clickedText[0])
                    else
                        viewModel.handleInput(v.clickedText[0])
                }
            is Button -> viewModel.handleInput(v.text[0])
        }
    }
}
