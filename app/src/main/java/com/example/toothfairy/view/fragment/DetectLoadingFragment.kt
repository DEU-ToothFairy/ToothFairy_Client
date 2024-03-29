package com.example.toothfairy.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.R
import com.example.toothfairy.viewModel.FaceDetectViewModel
import com.example.toothfairy.viewModel.SideFaceViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetectLoadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetectLoadingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var faceVM: FaceDetectViewModel
    private lateinit var sideVM: SideFaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        faceVM = ViewModelProvider(parentFragment as SideFaceCameraXFragment)[FaceDetectViewModel::class.java]
        sideVM = ViewModelProvider(parentFragment as SideFaceCameraXFragment)[SideFaceViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addObserver()
        return inflater.inflate(R.layout.fragment_detect_loading, container, false)
    }

    private fun addObserver(){
        faceVM.sideDetectResult.observe(viewLifecycleOwner){
            /**
             * 얼굴 인식 결과 사진이 오면 점수 데이터 요청
             */
            sideVM.getDetectResult()
        }

        sideVM.sideDetectResult.observe(viewLifecycleOwner){
            // 다음 프래그먼트에 출력
            (parentFragment as SideFaceCameraXFragment).childFragmentManager
                .beginTransaction()
                .replace(R.id.sideFaceFrameLayout, SideFaceResultFragment())
                .commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetectLoadingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetectLoadingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}