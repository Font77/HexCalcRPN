package com.alloydflanagan.hexcalcrpn.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.ColorInt
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
 * * `textColor` - The color of the text in labels on enabled buttons.
 * * `disabledTextColor` - The color of text in labels on disabled buttons.
 *
 * @param context The context in which the component is being rendered.
 * @param attrs Initial values of attributes.
 * @param defStyle Style to be used if none is set by `attrs`.
 */
class ButtonRowView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), OnClickListener {
    /** read only tag for log messages, etc. */
    @Suppress("UNUSED")
    val tag = "ButtonRowView"

    private val buttons = ArrayList<Button>()

    // would think this would be redundant, but initialization from XML apparently requires it
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    /**
     * The text label of the last button clicked by the user.
     */
    // ktlint is falsely saying this could be private, even though it's accessed in MainActivity
    @Suppress("PRIVATE")
    var clickedText = ""
        private set

    /**
     * The texts of button labels, separated by ";". Number of labels given determines the number
     * of buttons displayed.
     */
    private var buttonsText = ""
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
    private var textSize = 0f
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    /**
     * The color of the text labels.
     */
    @ColorInt
    var textColor = 0
        set(value) {
            field = value
            invalidate()
            // shouldn't change layout
        }

    @ColorInt
    var disabledTextColor = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * Set up component from XML attributes.
     */
    init {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.ButtonRowView, defStyle, 0)
        // do we need to do that again to get theme values for defaults??
        try {
            textSize = a.getDimension(R.styleable.ButtonRowView_textSize,
                12f)
            textColor = a.getColor(R.styleable.ButtonRowView_textColor,
                    Color.BLACK)
            disabledTextColor = a.getColor(R.styleable.ButtonRowView_disabledTextColor,
                    Color.GRAY)
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
        buttons.clear() // just in case
        for (p in buttonsText.split(";".toRegex())) {
            val btn = Button(context)
            // bit awkward setting color, possibly(?) because java ints are signed
            btn.setTextColor(Color.rgb(Color.red(textColor), Color.green(textColor), Color.blue(textColor)))
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            btn.text = p
            btn.setOnClickListener(this)
            addView(btn)
            btn.updateLayoutParams<LinearLayout.LayoutParams> {
                weight = 1.0f

            }
            buttons.add(btn)
        }
        Timber.d("Created buttons with textColor ${textColor.toString(16).toUpperCase()} and textSize $textSize")
    }

    fun disableButton(position: Int) {
        buttons[position].isEnabled = false
        buttons[position].isClickable = false
        buttons[position].setTextColor(Color.rgb(
                Color.red(disabledTextColor),
                Color.green(disabledTextColor),
                Color.blue(disabledTextColor)))
    }

    fun enableButton(position: Int) {
        buttons[position].isEnabled = true
        buttons[position].isClickable = true
        buttons[position].setTextColor(Color.rgb(
                Color.red(textColor),
                Color.green(textColor),
                Color.blue(textColor)))
    }

    @Suppress("UNUSED")
    fun isEnabled(position: Int): Boolean {
        return buttons[position].isEnabled
    }

    /**
     * Calls onClick handlers. Handlers can determine what key was clicked using the clickedText
     * property.
     */
    override fun onClick(view: View) {
        clickedText = (view as Button).text.toString()
        Timber.d("Click on button set clickedText to $clickedText")
        if (view.isEnabled) performClick()
    }
}
