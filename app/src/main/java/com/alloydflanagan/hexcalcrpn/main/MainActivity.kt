package com.alloydflanagan.hexcalcrpn.main

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.model.HexStack
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

    override fun onModesFragmentInteraction(bit_mode: BitsMode) = viewModel.handleModeInput(bit_mode)

    override fun onDigitsFragmentInteraction(digit: Char) = viewModel.handleInput(digit)

    override fun onOperatorFragmentInteraction(operator: String) = viewModel.handleInput(operator[0])

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_equals) {
            viewModel.handleInput('=')
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(hex_app_bar)
        // brv_modes.highlightedButton = BitsMode.INFINITE.toString()

        viewModel.getCurrent().observe(this, Observer {
            val fred = it?.toString(16)?.toUpperCase() ?: ""
            if (fred != tv_current.text) tv_current.text = fred
        })

        viewModel.getStack().observe(this, Observer {
            val txt = it.toString().toUpperCase()
            if (txt != tv_output.text) tv_output.text = txt
            brv_modes.highlightedButton = it.bits.toString()
        })

    }
}
