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

 * Activities that contain this fragment must implement the
 * [OperatorFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OperatorFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OperatorFragment : Fragment(), View.OnClickListener {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_operator, container, false)
        val brvList = arrayOf(R.id.brv_op_0, R.id.brv_op_1, R.id.brv_op_2, R.id.brv_op_3, R.id.brv_op_4)
        for (id in brvList) {
            val brv = view.findViewById<ButtonRowView>(id)
            brv.setOnClickListener(this)
        }
        return view
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val btn = v as ButtonRowView
            listener?.onOperatorFragmentInteraction(btn.clickedText)
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
        fun onOperatorFragmentInteraction(operator: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment OperatorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = OperatorFragment()
    }
}
