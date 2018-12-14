package com.alloydflanagan.hexcalcrpn.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.ui.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_word_size.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.math.BigInteger

/**
 * Main activity: displays output, _current entry, and keys. Handles key clicks.
 */
class MainActivity : AppCompatActivity(),
        OnClickListener,
        OperatorFragment.OnFragmentInteractionListener,
        DigitsFragment.OnFragmentInteractionListener,
        WordSizeFragment.OnFragmentInteractionListener,
        KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: AbstractStackViewModel<BigInteger> by instance()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onWordSizeFragmentInteraction(bits: BitsMode) {
        viewModel.handleModeInput(bits)
    }

    override fun onDigitsFragmentInteraction(digit: Char) = viewModel.handleInput(digit)

    override fun onOperatorFragmentInteraction(operator: String) = viewModel.handleOperator(operator[0])

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_equals) {
            viewModel.handleInput('=')
        }
        if (v != null) {
            Timber.d("onClick got view ID ${v.id} (${v.javaClass.name})")
        }
    }

    private fun selectCurrentBitsMode() {
        val bitSize = viewModel.getStack().value?.bits
        when (bitSize) {
            BitsMode.EIGHT -> radio_8.isChecked = true
            BitsMode.SIXTEEN -> radio_16.isChecked = true
            BitsMode.THIRTY_TWO -> radio_32.isChecked = true
            BitsMode.SIXTY_FOUR -> radio_64.isChecked = true
            BitsMode.INFINITE -> radio_inf.isChecked = true
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
            selectCurrentBitsMode()
        })

    }
}
