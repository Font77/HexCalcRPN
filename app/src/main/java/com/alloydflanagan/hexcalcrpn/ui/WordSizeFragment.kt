package com.alloydflanagan.hexcalcrpn.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.BitsMode
import timber.log.Timber

/**
 * A group of radio buttons for selecting a word size.
 *
 * Activities that contain this fragment must implement the
 * [WordSizeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class WordSizeFragment : Fragment(), View.OnClickListener {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_word_size, container, false)
        // why does setup default to making the Activity the OnClickListener? How is that modular?
        // and doesn't it conflict with the idea of [OnFragmentInteractionListener]

        // for some reason, kotlin's synthetic objects aren't set up here, so still have to
        // use [findViewById]
        val btns = arrayListOf(R.id.radio_8, R.id.radio_16,
                R.id.radio_32, R.id.radio_64, R.id.radio_inf)
        for (btn in btns) {
            val btnView = view.findViewById<RadioButton>(btn)
            btnView.setOnClickListener(this)
        }
        return view
    }

    private fun onSizeSelected(bits: BitsMode) {
        listener?.onWordSizeFragmentInteraction(bits)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.radio_8 -> onSizeSelected(BitsMode.EIGHT)
            R.id.radio_16 -> onSizeSelected(BitsMode.SIXTEEN)
            R.id.radio_32 -> onSizeSelected(BitsMode.THIRTY_TWO)
            R.id.radio_64 -> onSizeSelected(BitsMode.SIXTY_FOUR)
            R.id.radio_inf -> onSizeSelected(BitsMode.INFINITE)
            else -> Timber.w("Unexpected radio button ID: $v.id")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onWordSizeFragmentInteraction(bits: BitsMode)
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WordSizeFragment.
         */
        @Suppress("UNUSED")
        @JvmStatic
        fun newInstance() = WordSizeFragment()
    }
}
