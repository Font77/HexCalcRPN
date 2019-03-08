package com.alloydflanagan.hexcalcrpn.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
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
@Suppress("SpellCheckingInspection")
class ButtonRowView(context: Context,
                    attrs: AttributeSet? = null,
                    defStyle: Int = 0) : LinearLayoutCompat(context, attrs, defStyle), OnClickListener {

    private var listener: View.OnClickListener? = null

    private val buttons = ArrayList<AppCompatButton>()

    // would think this would be redundant, but initialization from XML apparently requires it
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    /**
     * The text label of the last button clicked by the user.
     */
    // ktlint is falsely saying this could be private, even though it's accessed in MainActivity
    @Suppress("PRIVATE", "SpellCheckingInspection")
    var clickedText = ""
        private set

    var clickedIndex: Int? = null
        private set

    /**
     * The texts of button labels, separated by ";". Number of labels given determines the number
     * of buttons displayed.
     */
    private var buttonsText = ""

    /**
     * The font size of the button labels.
     */
    private var textSize = 0f

    /**
     * The color of the text labels.
     */
    @ColorInt
    var textColor = 0

    @ColorInt
    var disabledTextColor = 0

    /**
     * Set up component from XML attributes.
     */
    init {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.ButtonRowView, defStyle, 0)
        // How do we get theme values for defaults??
        try {
            textSize = a.getDimension(R.styleable.ButtonRowView_textSize,
                spToPixels(20f))
            textColor = a.getColor(R.styleable.ButtonRowView_textColor,
                    Color.BLACK)
            disabledTextColor = a.getColor(R.styleable.ButtonRowView_disabledTextColor,
                    Color.GRAY)
//            <attr name="buttonPaddingVert" format="dimension" />
//            <attr name="buttonElevation" format="dimension" />
            // set text last, it will trigger creation of buttons
            buttonsText = a.getString(R.styleable.ButtonRowView_buttonsText) ?: ""
            createButtons()
        } finally {
            a.recycle()
        }
    }

    override fun setOnClickListener(ocl: OnClickListener) {
        listener = ocl
    }

    /**
     * Create a button for each label in `buttonsText`
     */
    private fun createButtons() {
        buttons.clear() // just in case
        for (p in buttonsText.split(";".toRegex())) {
            val btn = AppCompatButton(context)
            // bit awkward setting color, possibly(?) because java ints are signed
            btn.setTextColor(Color.rgb(Color.red(textColor), Color.green(textColor), Color.blue(textColor)))
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            btn.text = p
            btn.setOnClickListener(this)
            // btn.setBackgroundResource(R.drawable.button_states)
            addView(btn)
            btn.updateLayoutParams<LinearLayoutCompat.LayoutParams> {
                weight = 1.0f
            }
            buttons.add(btn)
        }
        Timber.d("Created buttons with textColor ${textColor.toString(16).toUpperCase()} and textSize $textSize")
    }

    @Suppress("UNUSED")
    fun disableButton(position: Int) {
        buttons[position].isEnabled = false
        buttons[position].isClickable = false
        buttons[position].setTextColor(Color.rgb(
                Color.red(disabledTextColor),
                Color.green(disabledTextColor),
                Color.blue(disabledTextColor)))
    }

    @Suppress("UNUSED")
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
     * Calls onClick handlers.
     *
     * Handlers can determine what key was clicked using the [clickedText] property. Disabled
     * buttons do NOT generate a click event.
     */
    override fun onClick(view: View) {
        val index = buttons.indexOf(view as AppCompatButton)
        if (index < 0) {
            Timber.w("onClick() failed to find button in buttons ArrayList!")
            return
        }
        val button = buttons[index]

        if (button.isEnabled) {
            clickedIndex = index
            clickedText = button.text.toString()
            listener?.onClick(this)
        } else {
            clickedIndex = null
            clickedText = ""
        }
    }

    /**
     * Convert from "dp" units to pixels using the metrics of the current display.
     *
     * Useful for providing default values for dimensional properties
     */
    @Suppress("UNUSED")
    private fun dpToPixels(dps: Float): Float {
        return resources.displayMetrics.density * dps
    }

    /**
     * Convert from "sp" units to pixels using the metrics of the current display.
     *
     * Useful for providing default values for dimensional properties
     */
    private fun spToPixels(sps: Float): Float {
        return resources.displayMetrics.scaledDensity * sps
    }
}
