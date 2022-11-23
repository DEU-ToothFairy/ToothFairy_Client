package com.example.toothfairy.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.example.toothfairy.R
import com.example.toothfairy.adapter.GuideAdapter
import com.example.toothfairy.databinding.FragmentGuideBinding
import com.example.toothfairy.util.Extention.hideBottomNabBar
import com.example.toothfairy.util.Extention.hideTitleBar
import com.example.toothfairy.util.Extention.showBottomNabBar
import com.example.toothfairy.util.Extention.showTitleBar
import com.example.toothfairy.util.GuideFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuideFragment : Fragment() {
    // TODO: Rename and change types of parameters

    // VARIABLE
    private lateinit var bind: FragmentGuideBinding
    private lateinit var guideType:String
    private var guideCount:Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            guideType = it.getString("GuideType")!!
            guideCount = it.getInt("GuideCount")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.apply {
            hideTitleBar()
            hideBottomNabBar()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_guide, container, false)
        bind.lifecycleOwner = requireActivity()

        if(::guideType.isInitialized){
            Log.i("가이드 데이터", "$guideType, $guideCount")
            bind.viewPager.apply {
                adapter = GuideAdapter(this@GuideFragment, guideType, guideCount) // 뷰페이저 어댑터

                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                currentItem = 0                 // 시작지점
                offscreenPageLimit = guideCount // 뷰페이저의 최대 이미지 수

                // 뷰페이저에 인디케이터 연결
                bind.indicator.attachTo(this)
            }
        }
        //putString("GuideType", "Facial")

        addStartedBtnClickEvent()

        return bind.root
    }

    private fun addStartedBtnClickEvent(){
        when(guideType){
            "Facial" -> {
                bind.startBtn.setOnClickListener {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, CameraXFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            "Protruding" -> {
                bind.startBtn.setOnClickListener {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, SideFaceCameraXFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            "Toothbrush" ->{
                bind.startBtn.setOnClickListener {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, ToothBrushCameraXFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.apply {
            showTitleBar()
            showBottomNabBar()
        }
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GuideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}