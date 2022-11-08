package com.example.toothfairy.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.toothfairy.R
import com.example.toothfairy.adapter.CardViewPagerAdapter
import com.example.toothfairy.databinding.FragmentExamineMenuBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExamineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExamineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind: FragmentExamineMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_examine_menu, container, false)
        
        // 뷰페이저 설정
        bind.viewPager.apply {
            adapter = CardViewPagerAdapter(this@ExamineFragment, PAGE_NUM)

            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            currentItem = 0        // 시작지점
            offscreenPageLimit = 3 // 최대 이미지 수
            clipToPadding = false  // 양 옆의 페이지가 보이기 위해서 설정
            clipChildren  = false  // 양 옆의 페이지가 보이기 위해서 설정
            
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
         * @return A new instance of fragment ExamineFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val PAGE_NUM = 3

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExamineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}