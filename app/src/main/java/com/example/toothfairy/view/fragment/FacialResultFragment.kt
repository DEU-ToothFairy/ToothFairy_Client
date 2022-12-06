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
import com.example.toothfairy.databinding.SubCenterStartProgressbarBinding
import com.example.toothfairy.dto.ResponseDto.FaqResDto
import com.example.toothfairy.util.Extention.getFaqList
import com.example.toothfairy.viewModel.FaceDetectViewModel
import com.google.mlkit.vision.face.FaceContour
import java.io.File
import kotlin.math.*

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
     * @param flag      : Int (왼쪽, 오른쪽을 정하는 변수)
     * @param progress  : Int
     */
    private fun SubCenterStartProgressbarBinding.setProgress(flag:Int, progress:Int){
        /**
         * 프로그레스의 최솟 값을 10으로 지정하기 위한 코드
         * 음수의 경우 절댓값을 취해서 비교하면 모두 양수가 되어버리므로,
         * progress / abs(progress)를 통해 progress의 부호를 적용
         */
        if (flag == 0 || progress == 0){
            this.leftProgressLayout.visibility = View.VISIBLE
            this.rightProgressLayout.visibility = View.INVISIBLE

            leftProgress.progress = 5
            leftProgressText.text = ""

            return
        }

        //val tempProgress = if(abs(progress) < 5) 5 * (progress / abs(progress)) else progress

        if(flag < 0){
            this.leftProgressLayout.visibility = View.VISIBLE
            this.rightProgressLayout.visibility = View.INVISIBLE

            leftProgress.progress = abs(progress)
            leftProgressText.text = "${progress}˚"
            leftText.setTextColor(resources.getColor(R.color.colorAccent))
            rightText.setTextColor(resources.getColor(R.color.text_black_gray))

        } else {
            this.leftProgressLayout.visibility = View.INVISIBLE
            this.rightProgressLayout.visibility = View.VISIBLE

            rightProgress.progress = progress
            rightProgressText.text = "${progress}˚"
            rightText.setTextColor(resources.getColor(R.color.colorAccent))
            leftText.setTextColor(resources.getColor(R.color.text_black_gray))
        }
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
                /**
                 * 안면 비대칭 타입 & 설명 표시 부분
                 * bind.typeResultTv.text = detect.type
                 * bind.explainTv.text = explainFactory(detect.type)
                 */
                initFaqRecylcerView(detect.type)
                Log.i("기울기 결과", detect.toString())
                bind.eyeDegreeText.text = if(detect.eyeSlope == 0) "${roundDigit(detect.eyeDegree, 2)}˚" else "${roundDigit(detect.eyeSlope * detect.eyeDegree, 2)}˚"
                bind.lipDegreeText.text = if(detect.lipSlope == 0) "${roundDigit(detect.lipDegree, 2)}˚" else "${roundDigit(detect.lipSlope * detect.lipDegree, 2)}˚"
                //bind.eyesDegreeProgress.setProgress(detect.eyeSlope, detect.eyeDegree.toInt())
                //bind.lipDegreeProgress.setProgress(detect.lipSlope, detect.lipDegree.toInt())
            }
        }
    }

    fun roundDigit(number: Double, digit:Int): Double{
        return (number * 10.0.pow(digit.toDouble())).roundToInt() / 10.0.pow(digit.toDouble())

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