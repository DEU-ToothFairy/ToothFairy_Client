package com.example.toothfairy.view.fragment

import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toothfairy.R
import com.example.toothfairy.adapter.ExamineFaqAdapter
import com.example.toothfairy.data.FaceType
import com.example.toothfairy.data.Faq
import com.example.toothfairy.databinding.FragmentFacialResultBinding
import com.example.toothfairy.dto.ResponseDto.FaqResDto
import com.example.toothfairy.util.Extention.getFaqList
import com.example.toothfairy.viewModel.FaceDetectViewModel
import com.google.mlkit.vision.face.FaceContour
import java.io.File
import kotlin.math.abs
import kotlin.math.atan2

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FacialResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FacialResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var imagePath: String? = null
    private lateinit var bind:FragmentFacialResultBinding
    private lateinit var faceVM:FaceDetectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        faceVM = ViewModelProvider(parentFragment as CameraXFragment)[FaceDetectViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_facial_result, container, false)
        arguments?.let {
            imagePath = it.getString("imagePath")
        }

        bind.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        imagePath?.let {
            val file: File = File(it)
            val bExist = file.exists()
            if (bExist) {
                val myBitmap = BitmapFactory.decodeFile(it)
                bind.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                bind.imageView.setImageBitmap(myBitmap)
            }

            Log.i("페이스 감정 결과", faceVM.face.toString())

            detectAsymmetry()
        }

        return bind.root
    }

    /**
     * Faq 리사이클러뷰 초기화
     */
    private fun initFaqRecylcerView(type:String){
        bind.faqRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ExamineFaqAdapter(faqFactory(type = type).getFaqList())
        }
    }

    /**
     * 비대칭 결과 표시 메소드
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun detectAsymmetry(){
        faceVM.face?.let {
            val detectResult = faceVM.detectAsymmetry()

            detectResult?.let { detect ->
                bind.typeResultTv.text = detect.type
                bind.explainTv.text = explainFactory(detect.type)
                setSegmentedProgress(detect.result)
                initFaqRecylcerView(detect.type)
            }
        }
    }

    private fun setSegmentedProgress(progress:Int){
        bind.segmentedProgressBar.apply {
            val enabled = arrayListOf<Int>()
            for (i in 0  until progress) enabled.add(i)

            if(enabled.isEmpty()) setEnabledDivisions(arrayListOf(0))
            else setEnabledDivisions(enabled)
        }
    }

    private fun setSegmentedProgress(eyeDegree:Double){
        bind.segmentedProgressBar.apply {
            val enabled = arrayListOf<Int>()
            for (i in 0  until (eyeDegree.toInt() / 10)) enabled.add(i)

            if(enabled.isEmpty()) setEnabledDivisions(arrayListOf(0))
            else setEnabledDivisions(enabled)
        }
    }

    /**
     * 안면비대칭 타입에 맞는 설명을 리턴하는 팩토리
     */
    private fun explainFactory(type: String): String{
        return when(type){
            "하악형 비대칭",
            "하악형 비대칭 의심" -> {
                resources.getString(R.string.facial_type1_explain)
            }
            "측두골형 비대칭" ->{
                resources.getString(R.string.facial_type2_explain)
            }
            "접형골형 비대칭" -> {
                resources.getString(R.string.facial_type3_explain)
            }
            else -> { // 정상인 경우
                resources.getString(R.string.facial_normal_explain)
            }
        }
    }

    /**
     * 안면 비대칭에 맞는 Faq를 리턴하는 팩토리
     */
    private fun faqFactory(type:String): FaqResDto.Faq{
        return when(type){
            "하악형 비대칭",
            "하악형 비대칭 의심" -> {
                FaqResDto.Faq(
                    question = resources.getStringArray(R.array.facial_type1_question),
                    answer = resources.getStringArray(R.array.facial_type1_answer),
                )
            }
            "측두골형 비대칭" ->{
                FaqResDto.Faq(
                    question = resources.getStringArray(R.array.facial_type2_question),
                    answer = resources.getStringArray(R.array.facial_type2_answer)
                )
            }
            "접형골형 비대칭" -> {
                FaqResDto.Faq(
                    question = resources.getStringArray(R.array.facial_type3_question),
                    answer = resources.getStringArray(R.array.facial_type3_answer)
                )
            }
            else -> { // 정상인 경우
                FaqResDto.Faq(
                    question = resources.getStringArray(R.array.facial_normal_question),
                    answer = resources.getStringArray(R.array.facial_normal_answer)
                )
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FacialResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FacialResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}