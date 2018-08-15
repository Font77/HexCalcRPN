package com.alloydflanagan.hexcalcrpn.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.alloydflanagan.hexcalcrpn.ui.ButtonRowView
import com.alloydflanagan.hexcalcrpn.R

/**
 * Main activity: displays output, current entry, and keys. Handles key clicks.
 */
class MainActivity : AppCompatActivity(), OnClickListener {

    @SuppressWarnings("unused")
    var hStack = HexStack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View) {
        val fred = v as ButtonRowView
        fred.textSize = 14f
        Toast.makeText(this, "got a click on ${fred.clickedText} button", Toast.LENGTH_SHORT).show()
    }
}
