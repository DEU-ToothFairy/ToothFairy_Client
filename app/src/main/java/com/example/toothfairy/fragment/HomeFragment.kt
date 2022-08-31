package com.example.toothfairy.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentHomeBinding
import com.example.toothfairy.databinding.WearingProgressLayoutBinding
import com.example.toothfairy.entity.CuredInfo
import com.example.toothfairy.util.DateManager
import com.example.toothfairy.viewmodel.BluetoothViewModel
import com.example.toothfairy.viewmodel.MainViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.util.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MyXAxisFormatter : ValueFormatter(){
    private val days = arrayOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()-1) ?: value.toString()
    }
}

class TimeAxisValueFormatter : IndexAxisValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()} hr"
    }
}

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    // VARIABLE
    lateinit var binding: FragmentHomeBinding // DataBinding
    lateinit var wearingBinding: WearingProgressLayoutBinding

    lateinit var mainViewModel: MainViewModel
    lateinit var bluetoothViewModel: BluetoothViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        wearingBinding = binding.wearingProgressLayout
        
        val view = binding.root

        // ViewModel 객체 연결
        bluetoothViewModel = BluetoothViewModel.instance
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // 데이터를 관리하는 뷰 모델을 binding에 연결해줘야 적용 됨
        binding.lifecycleOwner = requireActivity()
        binding.homeViewModel = mainViewModel

        // 오늘 날짜 설정
        mainViewModel.today.value = DateManager.today

        // 이벤트 등록 메소드
        patientEventAdder()
        curedEventAdder()
        bluetoothEventAdder()
        wearingTimeEventAdder()

        chart()

        return view
    }

    private fun chart(){
        val entries = ArrayList<BarEntry>()

        entries.add(BarEntry(1f,10.0f))
        entries.add(BarEntry(2f,12.0f))
        entries.add(BarEntry(3f,0.0f))
        entries.add(BarEntry(4f,15.0f))
        entries.add(BarEntry(5f,14.0f))
        entries.add(BarEntry(6f,24.0f))
        entries.add(BarEntry(7f,4.0f))

        val chart = binding.chart

        chart.run {
            description.isEnabled = false       // 차트 옆에 별도로 표기되는 description

            setMaxVisibleValueCount(7)          // 메뉴 개수
            setPinchZoom(false)                 // 핀치줌(두 손가락으로 줌인) 설정
            setDrawBarShadow(false)             // 그래프의 그림자
            setDrawGridBackground(false)        // 격자구조 설정
            setTouchEnabled(false)              // 차트 터치 막기
            animateY(1000)           // 밑에서부터 올라오는 애니매이션 적용
            extraBottomOffset = 10f             // 하단 여백 설정(이게 있어야 x축 라벨이 안 잘림)

            axisLeft.run {                      // Y 방향
                axisMaximum = 25f               // 100 위치에 선을 그리기 위해 101f로 최댓값 설정
                axisMinimum = 0f                // 최소값 0f
                granularity = 6f                // granularity(세분성) 50 단위마다 선을 그림
                // 위 설정이 20f 였으면 총 5개의 선이 그려짐

                valueFormatter = TimeAxisValueFormatter()
                setDrawLabels(true)             // 값 표시(0, 50, 100)
                setDrawAxisLine(false)          // 축 그리기 설정

                setDrawGridLines(true)          // 격자 라인 활용
                enableGridDashedLine(10f, 10f, 0f)

                axisLineColor = ContextCompat.getColor(context, R.color.colorAccent)    // 축 색깔 설정
                gridColor = ContextCompat.getColor(context, R.color.semi_gray)          // 격자 색깔 설정
                textColor = ContextCompat.getColor(context, R.color.gray)               // 라벨 텍스트 컬러 설정
                textSize = 12f                                                          // 라벨 텍스트 크기
            }

            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM       // X 축을 아래에 둠
                granularity = 1f                            // 간격을 1로 설정

                setDrawAxisLine(false)                       // 축 그림
                setDrawGridLines(false)                     // 격자

                valueFormatter = MyXAxisFormatter()
                textColor = ContextCompat.getColor(context, R.color.gray)  // 라벨 색상
                textSize = 12f                                             // 텍스트 크기

            }

            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            legend.isEnabled = false //차트 범례 설정
        }

        var set = BarDataSet(entries,"DataSet")//데이터셋 초기화 하기
        set.color = ContextCompat.getColor(requireContext()!!, R.color.colorAccent)

        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)

        val data = com.github.mikephil.charting.data.BarData(dataSet)
        data.barWidth = 0.25f //막대 너비 설정하기

        chart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            setRadius(50)
            setFitBars(true)
            invalidate()
        }
    }

    // 사용자 관련 이벤트 등록 메소드
    private fun patientEventAdder(){
        // 환자 정보가 갱신 된 경우
        // 치료 기간 설정 (현재 날짜 - 치료 시작 날짜)
        mainViewModel.patient.observe(requireActivity()){ patient ->
            mainViewModel.treatmentDays.value = patient?.startDate?.let { it -> DateManager.getElapsedDate(it) }

            setCalibrationProgress(mainViewModel.treatmentDays.value, mainViewModel.curedInfo.value)
        }

    }

    // 완치 환자 관련 이벤트 등록 메소드
    private fun curedEventAdder(){
        // 완치자 정보가 갱신 된 경우
        mainViewModel.curedInfo.observe(requireActivity()) { curedInfo ->
            setCalibrationProgress(mainViewModel.treatmentDays.value, curedInfo )
        }
    }

    // 블루투스 관련 이벤트 등록 메소드
    private fun bluetoothEventAdder(){
        // 착용 상태 감지
        bluetoothViewModel.wearStatus.observe(requireActivity()) { status: Boolean ->
            if (status) {
                wearingBinding.wearStatus.text = "현재 착용 중"
                wearingBinding.wearStatus.setTextColor(Color.parseColor("#6194f8"))
            } else {
                wearingBinding.wearStatus.text = "착용 대기 중"
                wearingBinding.wearStatus.setTextColor(Color.parseColor("#919191"))
            }
        }

        // 1분이 지났을 때 착용 중이라면 wearingFlag의 값이 바뀌도록 설정되어 있음
        bluetoothViewModel.wearingFlag.observe(requireActivity()) {
            mainViewModel.setDailyWearingTime(1000 * 60L)
            // 일일 착용 시간은 DailyWearingTime 변수와 바인딩 된게 아니라 getDailyWearingTimeToString()과 매핑 되어 있으므로
            // 옵저버로 감지 불가
            wearingBinding.wearTime.text = mainViewModel.dailyWearingTimeToString
        }
    }

    // 착용 시간 관련 이벤트 등록 메소드
    private fun wearingTimeEventAdder(){
        mainViewModel.dailyWearingTime.observe(requireActivity()){
            wearingBinding.wearingProgressBar.progress = (it.toDouble() / 86400000.0 * 100).toInt()
        }
    }

    // METHOD : 교정 진행률 프로그래스바 초기화 메소드
    private fun setCalibrationProgress(treatmentDays: Long?, curedInfo: CuredInfo?) {
        if (Objects.isNull(treatmentDays) || Objects.isNull(curedInfo)) return

        // 교정 진행 률 (환자 치료 기간 / 완치자의 총 치료 기간 * 100)
        val progress =
            (treatmentDays!! / curedInfo!!.totalTreatmentDate.toDouble() * 100).roundToInt().toDouble()

        // 교정 진행률 설정
        mainViewModel.calibrationProgress.value = progress
    }

    companion object {
        // 싱글턴 패턴
        var instance = HomeFragment()

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()

            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)

            fragment.arguments = args
            return fragment
        }
    }
}