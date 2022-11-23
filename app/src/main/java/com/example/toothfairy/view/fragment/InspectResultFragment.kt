package com.example.toothfairy.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.example.toothfairy.adapter.ExpandableFaqAdapter
import com.example.toothfairy.data.Faq
import com.example.toothfairy.databinding.FragmentInspectResultBinding
import com.example.toothfairy.util.Extention.*
import com.example.toothfairy.viewModel.FaceDetectViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
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
class InspectResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var image: ByteArray? = null
    private lateinit var bind:FragmentInspectResultBinding
    private var imagePath: String? = null
    private lateinit var faceVM: FaceDetectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getString("imagePath")
        }

        faceVM = ViewModelProvider(parentFragment as SideFaceCameraXFragment)[FaceDetectViewModel::class.java]
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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_inspect_result, container, false)
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

        /**
         * 측면 얼굴 결과는 뷰 모델에서 바로 가져옴
         * (Bundle로 보내면 용량이 너무 커서 에러남)
         */
        bind.imageView.setImageBitmap(faceVM.sideDetectResult.value)

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

    
    @RequiresApi(Build.VERSION_CODES.R)
    private fun detectAsymmetry(){
        faceVM.face?.let {
            /**
             * 
             * Atan2(x,y)*180/PI
             * 라인의 각도를 구하는 것이 핵심
             *
             * 1. 하악형
             *   - 눈은 일자, 아래턱(하악)이 좌측 혹은 우측으로 틀어진 경우
             *   - 아래턱 이외의 부위에서는 아직 비대칭이 뚜렷하게 나타나지 않은 상태
             *   - 타입 1이 방치되는 경우, 하악의 틀어짐이 더 심해지면서 타입 2로 심화됨
             *
             *   기울기 : 음수이면 오른쪽이 내려간 것(좌측이 올라감), 양수이면 오른쪽이 올라간 것(좌측이 내려감)
             *         : 눈의 기울기 소숫점 2번 째 자리까지 해서 백분율로 표현하기 (0.5를 최대치로 지정하여 비율 계산)
             *         : 후면 카메라로 하면 부호가 반대가 되므로 조심 (전면 카메라로 찍을 것을 권장)
             *
             * 2. 측두골형
             *   - 가장 흔한 안면 비대칭 유형
             *   - 위턱(상악)과 아래턱(하악)이 같은 방향으로 틀어짐
             *   - 아래턱이 틀어진 방향으로 눈과 광대가 주저앉으면서 얼굴이 찌그러져 보이는 유형
             *   - 아래턱이 틀어진 방향으로 코가 휘는 경우가 많음
             *   - 노화가 진행되면 찌그러진 방향으로 눈가 주름, 팔자 주름 등도 더 깊이 유발 됨
             *
             * 3. 접형골형
             *   - 가장 심한 안면 비대칭 유형
             *   - 타입2에서 비대칭이 더욱 진행되어 측두골과 접형골 등 두개골의 변위 진행
             *   - 아래턱과 반대방향으로 위턱이 틀어지고 코가 휨
             *   - 안면뼈와 두개골의 벼위가 가장 많이 진행된 유형으로 치료기간이 길어짐
             */
            
            // 왼쪽 눈, 오른쪽 눈 PointF() 객체
            val leftEye = it.getContour(FaceContour.LEFT_EYE)
            val rightEye = it.getContour(FaceContour.RIGHT_EYE)
            val lip = it.getContour(FaceContour.UPPER_LIP_TOP)

            if(leftEye != null && rightEye != null && lip != null){
                // Atan2(x,y)*180/PI
                val xDis = leftEye.points[0].x.minus(rightEye.points[0].x).toDouble()
                val yDis = leftEye.points[0].y.minus(rightEye.points[8].y).toDouble()
                // val result = atan2(abs(point2.y-point1.y).toDouble(), abs(point2.x-point1.x).toDouble())
                // 눈 기울기
                val eyeIncline = leftEye.points[0].y.minus(rightEye.points[8].y) / leftEye.points[0].x.minus(rightEye.points[0].x)
                // 눈 각도
                val eyeDegree = atan2(abs(yDis), abs(xDis)) * 180 / Math.PI // degree 각도

                val leftLip = lip.points[0]     // 입술 왼쪽 끝 점
                val rightLip = lip.points[10]   // 입술 오른쪽 끝 점
                val lipXdis = leftLip.x.minus(rightLip.x)
                val lipYdis = leftLip.y.minus(rightLip.y)

                // 입술 기울기
                val lipIncline = leftLip.y.minus(rightLip.y) / leftLip.x.minus(rightLip.x)
                // 입술 각도
                val lipDegree = atan2(abs(lipYdis), abs(lipXdis)) * 180 / Math.PI // degree 각도

                Log.i("양쪽 눈의 기울기","$eyeIncline") // 음수면 오른 쪽이 내려간 것
                Log.i("눈 비대칭 정도 : ", "${eyeDegree / 20 * 100}") // 20을 임의의 최댓 값으로 지정

                Log.i("양쪽 입술의 기울기","$lipIncline")
                Log.i("입술 비대칭 정도 : ", "${lipDegree / 20 * 100}")

                setSegmentedProgress((eyeDegree / 13 * 100) + (lipDegree / 13 * 100))
            }
            //checkAsymmetry()
        }
    }

    private fun checkAsymmetry(){
        faceVM.face?.let {
            val noseBridge = it.getContour(FaceContour.NOSE_BRIDGE)
            noseBridge?.let { nodeBridge ->
                val allContours = it.allContours

                val contourPoints = arrayListOf<PointF>()
                allContours.forEach { cont->
                    contourPoints.addAll(cont.points)
                }
                // Log.i("모든 점 사이즈", allContours.size.toString())
                // Log.i("모든 점", allContours.toString())

                var leftPoint = 0
                var rightPoint = 0
                contourPoints.forEach { point ->
                    if(nodeBridge.points[0].x < point.x) leftPoint++
                    else if(nodeBridge.points[0].x > point.x) rightPoint++
                }

                /**
                 * 후면 카메라로 찍으면 좌우 반전돼서 개수 반대로 나옴
                 */
                Log.i("왼쪽 점", leftPoint.toString())
                Log.i("오른쪽 점", rightPoint.toString())

                //setSegmentedProgress(abs(leftPoint - rightPoint))
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

    private fun loadData():List<Faq> {
        val faqList = ArrayList<Faq>()

        faqList.add(
            Faq(
            question = "교정 치료, 왜 필요한가요 ?",
            answer = """ 교정치료를 하는 목적은 크게 두 가지로 말할 수 있습니다. 첫째는 기능의 향상이고, 둘째는 심미적인 향상입니다. 

 즉, 덧니를 교정치료 하면 음식물을 씹을 때 효율이 좋아집니다. 치아교정을 한 후에 얼굴이 많이 예뻐진 사람들을 주위에서 보았을 것입니다.   

 덧니나 뻐드렁니로 인해서 보기 흉하던 입 모양이나 튀어 나온 입 모양을 바꾸어 줄 수 있습니다. 
 
 그 외에도 교정치료는 여러 가지 이유로 해서 필요로 합니다.
            """.trimIndent(),
            isExpanded = true
        )
        )

        faqList.add(
            Faq(
            question = "교정 치료는 언제하는게 좋나요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        )
        )

        faqList.add(
            Faq(
            question = "교정 치료는 아픈가요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        )
        )

        faqList.add(
            Faq(
            question = "교정 치료는 얼마나 걸리나요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        )
        )

        faqList.add(
            Faq(
            question = "교정 장치는 어떤 종류가 있나요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        )
        )

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
            InspectResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}