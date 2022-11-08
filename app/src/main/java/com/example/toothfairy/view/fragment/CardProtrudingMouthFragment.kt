package com.example.toothfairy.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentCardProtrudingMouthBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CardProtrudingMouthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardProtrudingMouthFragment : Fragment() {

    // VARIABLE
    private lateinit var bind:FragmentCardProtrudingMouthBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_card_protruding_mouth, container, false)

        bind.parentLayout.setOnClickListener{
            // GuideType을 Fragmet-Ktx로 전달
            requireActivity().supportFragmentManager.apply {
                setFragmentResult("GuidePageData", bundleOf(
                    "GuideType" to "Protruding",
                    "GuideCount" to PAGE_NUM
                ))

                beginTransaction()
                    .replace(R.id.frameLayout, GuideFragment())
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
         * @return A new instance of fragment CardProtrudingMouthFragment.
         */
        // TODO: Rename and change types and number of parameters
        const val PAGE_NUM = 3
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CardProtrudingMouthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}