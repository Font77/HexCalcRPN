package com.alloydflanagan.hexcalcrpn.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alloydflanagan.hexcalcrpn.R


/**
 * A simple [Fragment] subclass to display a grid of buttons denoting operators.
 *
 * Activities that contain this fragment must implement the
 * [OperatorFragment.OnFragmentInteractionListener] interface to handle interaction events.
 *
 * Use the [OperatorFragment.newInstance] factory method to create an instance of this fragment.
 */
class OperatorFragment : Fragment(), View.OnClickListener {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_operator, container, false)
        /*
         * Default click listener is containing activity, not fragment (why?????).
         * We want to communicate through [OnFragmentInteractionListener] only. So, reset click
         * listeners to this fragment.
         */

        val brvList = arrayOf(R.id.brv_op_0, R.id.brv_op_1, R.id.brv_op_2, R.id.brv_op_3, R.id.brv_op_4)
        for (id in brvList) {
            val brv = view.findViewById<ButtonRowView>(id)
            brv.setOnClickListener(this)
        }
        return view
    }

    /**
     * Dispatch clicked key to listeners using [OnFragmentInteractionListener] interface.
     */
    override fun onClick(v: View?) {
        if (v != null) {
            val btn = v as ButtonRowView
            listener?.onOperatorFragmentInteraction(btn.clickedText)
        }
    }

    /**
     * Verify that container can receive events from this fragment.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this fragment.
     *
     * [onOperatorFragmentInteraction] will be called whenever a key is pressed, and will be passed
     * the text of that key.
     */
    interface OnFragmentInteractionListener {
        fun onOperatorFragmentInteraction(operator: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment.
         *
         * @return A new instance of fragment OperatorFragment.
         */
        @JvmStatic
        @Suppress("unused")
        fun newInstance() = OperatorFragment()
    }
}
