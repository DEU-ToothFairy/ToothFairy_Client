package com.example.toothfairy.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.toothfairy.R
import com.example.toothfairy.adapter.GuideAdapter
import com.example.toothfairy.databinding.FragmentFacialAsymmetryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FacialAsymmetryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FacialAsymmetryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind: FragmentFacialAsymmetryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_facial_asymmetry, container, false)

        bind.viewPager.apply {
            adapter = GuideAdapter(this@FacialAsymmetryFragment, "Facial", FACIAL_PAGE_NUM)

            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            currentItem = 0        // 시작지점
            offscreenPageLimit = 3 // 최대 이미지 수

            // 뷰페이저에 인디케이터 연결
            bind.indicator.attachTo(this)
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
         * @return A new instance of fragment FacialAsymmetryFragment.
         */

        const val FACIAL_PAGE_NUM = 3
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FacialAsymmetryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}