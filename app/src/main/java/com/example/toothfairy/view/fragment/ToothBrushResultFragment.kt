package com.example.toothfairy.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toothfairy.R
import com.example.toothfairy.adapter.ExamineFaqAdapter
import com.example.toothfairy.data.Faq
import com.example.toothfairy.databinding.FragmentToothBrushResultBinding
import com.example.toothfairy.util.Extention.*
import com.example.toothfairy.viewModel.FaceDetectViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ToothBrushResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ToothBrushResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind:FragmentToothBrushResultBinding
    private lateinit var faceVM: FaceDetectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        faceVM = ViewModelProvider(parentFragment as ToothBrushCameraXFragment)[FaceDetectViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.apply {
            hideTitleBar()
            hideBottomNabBar()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_tooth_brush_result, container, false)
        bind.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        /**
         * 측면 얼굴 결과는 뷰 모델에서 바로 가져옴
         * (Bundle로 보내면 용량이 너무 커서 에러남)
         */
        bind.imageView.setImageBitmap(faceVM.toothBrushResult.value)

        initFaqRecylcerView()
        //makeBitmap()
        addClickEventNextBtn()

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

    private fun loadData():List<Faq> {
        val faqList = ArrayList<Faq>()
        val questionList = resources.getStringArray(R.array.toothbrush_question)
        val answerList = resources.getStringArray(R.array.toothbrush_answer)

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
         * @return A new instance of fragment ToothBrushResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ToothBrushResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}