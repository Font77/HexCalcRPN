package com.alloydflanagan.hexcalcrpn.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alloydflanagan.hexcalcrpn.R
import com.alloydflanagan.hexcalcrpn.model.BitsMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_BIT_MODE = "bit_mode"

/**
 * A [Fragment] subclass to present a row of buttons to select a bit mode.
 *
 * The button for the current bit mode is highlighted.
 *
 * Activities that contain this fragment must implement the
 * [ModesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ModesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ModesFragment : Fragment(), View.OnClickListener {
    /** track chosen bit mode so we can highlight corresponding button */
    private var bit_mode = BitsMode.INFINITE
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val bmode = it.getString(ARG_BIT_MODE)
            if (bmode != null) bit_mode = BitsMode.fromString(bmode)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_modes, container, false)
        val modes = view.findViewById<ButtonRowView>(R.id.brv_modes)
        modes.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        bit_mode = BitsMode.fromString((v as ButtonRowView).clickedText)
        listener?.onModesFragmentInteraction(bit_mode)
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
        // TODO: Update argument type and name
        fun onModesFragmentInteraction(bit_mode: BitsMode)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param bit_mode The initial bit mode.
         * @return A new instance of fragment ModesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bit_mode: BitsMode) =
                ModesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_BIT_MODE, bit_mode.toString())
                    }
                }
    }
}
