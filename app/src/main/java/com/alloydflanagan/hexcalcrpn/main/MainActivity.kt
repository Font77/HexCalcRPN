package com.alloydflanagan.hexcalcrpn.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.lifecycle.Observer
import com.alloydflanagan.hexcalcrpn.ui.ButtonRowView
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.ui.AbstractStackViewModel
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
        })
    }

    override fun onClick(v: View) {
        if (v is ButtonRowView) {
            viewModel.handleInput(v.clickedText[0])
        } else if (v is Button) {
            viewModel.handleInput(v.text[0])
        }
    }
}
