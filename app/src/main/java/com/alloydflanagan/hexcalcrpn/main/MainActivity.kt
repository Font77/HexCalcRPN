package com.alloydflanagan.hexcalcrpn.main

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.ReadStack
import com.alloydflanagan.hexcalcrpn.ui.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_modes.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.math.BigInteger

/**
 * Main activity: displays output, _current entry, and keys. Handles key clicks.
 */
class MainActivity : AppCompatActivity(),
        OnClickListener,
        ModesFragment.OnFragmentInteractionListener,
        OperatorFragment.OnFragmentInteractionListener,
        DigitsFragment.OnFragmentInteractionListener,
        KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: AbstractStackViewModel<BigInteger> by instance()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(hex_app_bar)

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
        // some operations don't make sense in some modes
//        if (stack.bits == BitsMode.INFINITE) {
//            brv_2.disableButton(5)  // '~'
//            brv_modes.disableButton(5) // SIGN
//        } else {
//            brv_2.enableButton(5)  // '~'
//            brv_modes.enableButton(5) // SIGN
//        }
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
