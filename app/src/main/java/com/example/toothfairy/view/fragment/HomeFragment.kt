package com.example.toothfairy.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentHomeBinding
import com.example.toothfairy.entity.CuredInfo
import com.example.toothfairy.util.TimeManager
import com.example.toothfairy.viewModel.BluetoothViewModel
import com.example.toothfairy.viewModel.HomeViewModel
import com.example.toothfairy.viewModel.MainViewModel
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
    private lateinit var binding: FragmentHomeBinding // DataBinding

    private lateinit var mainVM: MainViewModel
    private lateinit var homeVM: HomeViewModel
    private lateinit var blueVM: BluetoothViewModel

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

        /** ViewModel 객체 연결 */
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        homeVM = ViewModelProvider(this)[HomeViewModel::class.java]
        blueVM = BluetoothViewModel //ViewModelProvider(requireActivity())[BluetoothViewModel::class.java]

        /** 데이터를 관리하는 뷰 모델을 binding에 연결해줘야 적용 됨 */
        binding.lifecycleOwner = requireActivity()
        binding.mainVM = mainVM
        binding.homeVM = homeVM

        /** 이벤트 등록 */
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
        mainVM.patient.observe(requireActivity()){ patient ->
            mainVM.treatmentDays.value = patient?.startDate?.let { it -> TimeManager.getElapsedDate(it) }

            setCalibrationProgress(mainVM.treatmentDays.value, mainVM.curedInfo.value)
        }
    }

    /** 완치 환자 관련 이벤트 등록 메소드 */
    private fun curedEventAdder(){
        // 완치자 정보가 갱신 된 경우
        mainVM.curedInfo.observe(requireActivity()) { curedInfo ->
            setCalibrationProgress(mainVM.treatmentDays.value, curedInfo )
        }
    }

    /** 블루투스 관련 이벤트 등록 메소드 */
    private fun bluetoothEventAdder(){
        // 블루투스 데이터 값에 따라 wearStatus 값을 변경
        // 이후 블루투스 데이터의 형식을 변경해야 할 수 있으므로 이벤트를 따로 분리
        blueVM.bluetoothData.observe(requireActivity()) { value: String ->
            if (value == "ON") blueVM!!.wearStatus.setValue(true)
            else blueVM!!.wearStatus.setValue(false)
        }

        // 착용 상태 감지
        blueVM.wearStatus.observe(requireActivity()) { status: Boolean ->
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
        blueVM.wearingFlag.observe(requireActivity()) {
            mainVM.setDailyWearingTime(1000 * 60L)

            // 일일 착용 시간은 DailyWearingTime 변수와 바인딩 된게 아니라 getDailyWearingTimeToString()과 매핑 되어 있으므로
            // 옵저버로 감지 불가
            binding.wearingTimeTv.text = mainVM.dailyWearingTimeToString
            
            // 일일 착용 시간이 갱신 될 때 남은 착용 시간도 같이 갱신
            mainVM.dailyWearingTime.value?.let { it -> homeVM.updateRemainTime(it) }
            Log.i("REMAIN TO STRING", homeVM.remainWearingTimeToString())
            binding.remainWearingTimeTv.text = homeVM.remainWearingTimeToString()
        }
    }

    /** 교정 진행률(치료일 수 / 완치 환자의 평균 치료일 수) 프로그래스바 초기화 메소드 */
    private fun setCalibrationProgress(treatmentDays: Long?, curedInfo: CuredInfo?) {
        if (Objects.isNull(treatmentDays) || Objects.isNull(curedInfo)) return

        // 교정 진행 률 (환자 치료 기간 / 완치자의 총 치료 기간 * 100)
        val progress =
            (treatmentDays!! / curedInfo!!.totalTreatmentDate.toDouble() * 100).roundToInt().toDouble()

        // 교정 진행률 설정
        mainVM.calibrationProgress.value = progress
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