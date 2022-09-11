package com.example.toothfairy.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentHomeBinding
import com.example.toothfairy.entity.CuredInfo
import com.example.toothfairy.util.DateManager
import com.example.toothfairy.viewModel.BluetoothViewModel
import com.example.toothfairy.viewModel.HomeViewModel
import com.example.toothfairy.viewModel.MainViewModel
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


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    // VARIABLE
    lateinit var binding: FragmentHomeBinding // DataBinding

    lateinit var mainViewModel: MainViewModel
    lateinit var homeViewModel: HomeViewModel
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

        val view = binding.root

        // ViewModel 객체 연결
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        bluetoothViewModel = BluetoothViewModel //ViewModelProvider(requireActivity())[BluetoothViewModel::class.java]

        // 데이터를 관리하는 뷰 모델을 binding에 연결해줘야 적용 됨
        binding.lifecycleOwner = requireActivity()
        binding.mainViewModel = mainViewModel
        binding.homeViewModel = homeViewModel

        // 오늘 날짜 설정
        //mainViewModel.today.value = DateManager.today

        // 이벤트 등록 메소드
        patientEventAdder()
        curedEventAdder()
        bluetoothEventAdder()

        binding.checklistEditBtn.setOnClickListener {
            var bottomSheet = ChecklistBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        return view
    }



    /** 사용자 관련 이벤트 등록 메소드 */
    private fun patientEventAdder(){
        // 환자 정보가 갱신 된 경우
        // 치료 기간 설정 (현재 날짜 - 치료 시작 날짜)
        mainViewModel.patient.observe(requireActivity()){ patient ->
            mainViewModel.treatmentDays.value = patient?.startDate?.let { it -> DateManager.getElapsedDate(it) }

            setCalibrationProgress(mainViewModel.treatmentDays.value, mainViewModel.curedInfo.value)
        }

        // 남은 착용 시간 갱신
        mainViewModel.dailyWearingTime.observe(requireActivity()){daily ->
            homeViewModel.remainWearingTime.value?.let {
                mainViewModel.targetWearingTime.value?.minus(daily) // 목표 착용 시간 - 일일 착용 시간 = 남은 착용 시간
            }

            binding.remainWearingTimeTv.text = homeViewModel.remainWearingTimeToString()
        }
    }

    /** 완치 환자 관련 이벤트 등록 메소드 */
    private fun curedEventAdder(){
        // 완치자 정보가 갱신 된 경우
        mainViewModel.curedInfo.observe(requireActivity()) { curedInfo ->
            setCalibrationProgress(mainViewModel.treatmentDays.value, curedInfo )
        }
    }

    /** 블루투스 관련 이벤트 등록 메소드 */
    private fun bluetoothEventAdder(){
        bluetoothViewModel.bluetoothData.observe(requireActivity()) { value: String ->
            Log.i("이벤트", value)
            if (value == "ON") bluetoothViewModel!!.wearStatus.setValue(true)
            else bluetoothViewModel!!.wearStatus.setValue(false)
        }

        // 착용 상태 감지
        bluetoothViewModel.wearStatus.observe(requireActivity()) { status: Boolean ->
            Log.i("STATUS", "$status")
            if (status) {
                binding.wearingStatTv.text = "착용 중"
                binding.wearingStatTv.setTextColor(Color.parseColor("#6194f8"))
                binding.wearingStatusView.setCardBackgroundColor(Color.parseColor("#E9F2FF"))
            } else {
                binding.wearingStatTv.text = "착용 대기 중"
                binding.wearingStatTv.setTextColor(Color.parseColor("#919191"))
                binding.wearingStatusView.setCardBackgroundColor(Color.parseColor("#ECECEC"))
            }
        }

        // 1분이 지났을 때 착용 중이라면 wearingFlag의 값이 바뀌도록 설정되어 있음
        bluetoothViewModel.wearingFlag.observe(requireActivity()) {
            mainViewModel.setDailyWearingTime(1000 * 60L)
            // 일일 착용 시간은 DailyWearingTime 변수와 바인딩 된게 아니라 getDailyWearingTimeToString()과 매핑 되어 있으므로
            // 옵저버로 감지 불가
            binding.wearingTimeTv.text = mainViewModel.dailyWearingTimeToString
        }
    }

    /** 교정 진행률(치료일 수 / 완치 환자의 평균 치료일 수) 프로그래스바 초기화 메소드 */
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