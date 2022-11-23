package com.example.toothfairy.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentCardToothbrushBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CardToothbrushFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class CardToothbrushFragment : Fragment() {

    // VARIABLE
    private lateinit var bind:FragmentCardToothbrushBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_card_toothbrush, container, false)

        bind.parentLayout.setOnClickListener{
            requireActivity().supportFragmentManager.apply {
                val fragment = GuideFragment().apply {
                    arguments = Bundle().apply {
                        putString("GuideType", "Toothbrush")
                        putInt("GuideCount", PAGE_NUM)
                    }
                }

                beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return bind.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CardToothbrushFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val PAGE_NUM = 2
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CardToothbrushFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}