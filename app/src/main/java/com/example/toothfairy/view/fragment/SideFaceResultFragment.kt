package com.example.toothfairy.view.fragment

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toothfairy.R
import com.example.toothfairy.adapter.ExamineFaqAdapter
import com.example.toothfairy.data.Faq
import com.example.toothfairy.databinding.FragmentSideFaceResultBinding
import com.example.toothfairy.util.Extention.*
import com.example.toothfairy.viewModel.FaceDetectViewModel
import com.example.toothfairy.viewModel.SideFaceViewModel
import com.google.mlkit.vision.face.*
import java.io.File
import kotlin.math.abs
import kotlin.math.atan2


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InspectResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SideFaceResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind:FragmentSideFaceResultBinding
    private lateinit var faceVM: FaceDetectViewModel
    private lateinit var sideVM: SideFaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        faceVM = ViewModelProvider(parentFragment as SideFaceCameraXFragment)[FaceDetectViewModel::class.java]
        sideVM = ViewModelProvider(parentFragment as SideFaceCameraXFragment)[SideFaceViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.apply {
            hideTitleBar()
            hideBottomNabBar()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_side_face_result, container, false)
        bind.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        /**
         * 측면 얼굴 결과는 뷰 모델에서 바로 가져옴
         * (Bundle로 보내면 용량이 너무 커서 에러남)
         */
        bind.imageView.setImageBitmap(faceVM.sideDetectResult.value)
        Log.d("옆면 결과", sideVM.sideDetectResult.toString())

        initFaqRecylcerView()
        //makeBitmap()
        addClickEventNextBtn()

        sideVM.sideDetectResult.value?.let {
            bind.pointDistanceText.text = "${it.score}"
        }

        return bind.root
    }

    private fun addClickEventNextBtn(){
        bind.nextTv.setOnClickListener{
            this.backToPrevious()
        }
    }

    /**
     * Faq 리사이클러뷰 초기화
     */
    private fun initFaqRecylcerView(){
        bind.faqRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ExamineFaqAdapter(loadData())
        }
    }


//    /**
//     * 사각형 프로그레스바 세팅 메소드
//     */
//    private fun setSegmentedProgress(progress:Int){
//        bind.segmentedProgressBar.apply {
//            val enabled = arrayListOf<Int>()
//            for (i in 0  until progress) enabled.add(i)
//
//            if(enabled.isEmpty()) setEnabledDivisions(arrayListOf(0))
//            else setEnabledDivisions(enabled)
//        }
//    }
//    private fun setSegmentedProgress(eyeDegree:Double){
//        bind.segmentedProgressBar.apply {
//            val enabled = arrayListOf<Int>()
//            for (i in 0  until (eyeDegree.toInt() / 10)) enabled.add(i)
//
//            if(enabled.isEmpty()) setEnabledDivisions(arrayListOf(0))
//            else setEnabledDivisions(enabled)
//        }
//    }

    private fun loadData():List<Faq> {
        val faqList = ArrayList<Faq>()
        val questionList = resources.getStringArray(R.array.facial_type1_question)
        val answerList = resources.getStringArray(R.array.facial_type1_answer)

        for (i in questionList.indices){
            faqList.add(
                Faq(
                    question = questionList[i],
                    answer = answerList[i].trimIndent(),
                )
            )
        }

        return faqList
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
         * @return A new instance of fragment InspectResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SideFaceResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}