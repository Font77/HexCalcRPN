package com.alloydflanagan.hexcalcrpn.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alloydflanagan.hexcalcrpn.R
import timber.log.Timber

/**
 * A simple [Fragment] subclass to display a 4 x 4 grid of buttons labeled with hex digits.
 *
 * Activities that contain this fragment must implement the
 * [DigitsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DigitsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DigitsFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_digits, container, false)
        // don't know why the kotlin synthetic variables are null here, but they are
        val digits0 = view.findViewById<ButtonRowView>(R.id.brv_digits_0)
        digits0.setOnClickListener(this)
        val digits4 = view.findViewById<ButtonRowView>(R.id.brv_digits_4)
        digits4.setOnClickListener(this)
        val digits8 = view.findViewById<ButtonRowView>(R.id.brv_digits_8)
        digits8.setOnClickListener(this)
        val digitsC = view.findViewById<ButtonRowView>(R.id.brv_digits_C)
        digitsC.setOnClickListener(this)
        return view
    }

    override fun onClick(brv: View?) {
        Timber.d("got click event from $brv")
        listener?.onDigitsFragmentInteraction((brv as ButtonRowView).clickedText[0])
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
        fun onDigitsFragmentInteraction(digit: Char)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment DigitsFragment.
         */
        @JvmStatic
        fun newInstance() = DigitsFragment()
    }
}
