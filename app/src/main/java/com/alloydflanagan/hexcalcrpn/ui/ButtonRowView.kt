package com.alloydflanagan.hexcalcrpn.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.alloydflanagan.hexcalcrpn.R
import timber.log.Timber

/**
 * A component to display one or more buttons in a horizontal row.
 *
 * See {@link android.widget.LinearLayout LinearLayout} for layout attributes, etc.
 *
 * ## Attributes
 *
 * * `buttonsText` - A string containing the text label of each button, in order, separated by ";"
 *   The number of buttons is determined by the number of labels.
 * * `textSize` - The size of the text in button labels.
 * * `textColor` - The color of the text in button labels.
 *
 * @param context The context in which the component is being rendered.
 * @param attrs Initial values of attributes.
 * @param defStyle Style to be used if none is set by `attrs`.
 */
class ButtonRowView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), OnClickListener {
    /** read only tag for log messages, etc. */
    val tag = "ButtonRowView"

    // would think this would be redundant, but initialization from XML apparently requires it
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    /**
     * The text label of the last button clicked by the user.
     */
    var clickedText = ""
        private set
    /**
     * The texts of button labels, separated by ";". Number of labels given determines the number
     * of buttons displayed.
     */
    var buttonsText = ""
        set(value) {
            field = value
            removeAllViewsInLayout()
            createButtons()
            invalidate()
            requestLayout()
        }
    /**
     * The font size of the button labels.
     */
    var textSize = 0f
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    /**
     * The color of the text labels.
     */
    var textColor = 0
        set(value) {
            field = value
            invalidate()
            // shouldn't change layout
        }

    /**
     * Set up component from XML attributes.
     */
    init {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.ButtonRowView, defStyle, 0)
        try {
            textSize = a.getDimension(R.styleable.ButtonRowView_textSize,
                12f)
            textColor = a.getColor(R.styleable.ButtonRowView_textColor,
                0xFFFFFF)
            // set text last, it will trigger creation of buttons
            buttonsText = a.getString(R.styleable.ButtonRowView_buttonsText) ?: ""
        } finally {
            a.recycle()
        }
    }

    /**
     * Create a button for each label in `buttonsText`
     */
    private fun createButtons() {
        for (p in buttonsText.split(";".toRegex())) {
            val btn = Button(context)
            // bit awkward setting color, possibly(?) because java ints are signed
            btn.setTextColor(Color.argb(0xFF, Color.red(textColor), Color.green(textColor), Color.blue(textColor)))
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            btn.text = p
            btn.setOnClickListener(this)
            addView(btn)
            btn.updateLayoutParams<LinearLayout.LayoutParams> {
                weight = 1.0f

            }
        }
        Timber.d("Created buttons with textColor ${textColor.toString(16).toUpperCase()} and textSize $textSize")
    }


    /**
     * Calls onClick handlers. Handlers can determine what key was clicked using the clickedText
     * property.
     */
    override fun onClick(view: View) {
        clickedText = (view as Button).text.toString()
        Timber.d("Click on button set clickedText to $clickedText")
        performClick()
    }
}
