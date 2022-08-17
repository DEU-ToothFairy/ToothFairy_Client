package com.example.toothfairy.fragment

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentHomeBinding
import com.example.toothfairy.entity.CuredInfo
import com.example.toothfairy.util.DateManager
import com.example.toothfairy.viewModel.BluetoothViewModel
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
    lateinit var binding: FragmentHomeBinding // DataBinding
    lateinit var viewModel: MainViewModel
    lateinit var bluetoothViewModel: BluetoothViewModel
    var db: SQLiteDatabase? = null

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
        bluetoothViewModel = BluetoothViewModel.instance
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // 데이터를 관리하는 뷰 모델을 binding에 연결해줘야 적용 됨
        binding.lifecycleOwner = requireActivity()
        binding.homeViewModel = viewModel

        // 오늘 날짜 설정
        viewModel.today.value = DateManager.today

        val treatmentDays:MutableLiveData<Long>                 = viewModel.treatmentDays
        val curedInfoMutableLiveData:MutableLiveData<CuredInfo?> = viewModel.curedInfo

        // 환자 정보가 갱신 된 경우
        // 치료 기간 설정 (현재 날짜 - 치료 시작 날짜)
        viewModel.patient.observe(requireActivity()){ patient ->
            treatmentDays.value = patient?.startDate?.let { it -> DateManager.getElapsedDate(it) }

            setCalibrationProgress(treatmentDays.value, curedInfoMutableLiveData.value)
        }

        // 완치자 정보가 갱신 된 경우
        viewModel.curedInfo.observe(requireActivity()) { curedInfo ->
            setCalibrationProgress(treatmentDays.value, curedInfo )
        }

        bluetoothViewModel.wearStatus.observe(requireActivity()) { status: Boolean ->
            if (status) {
                binding.wearStatus.text = "현재 착용 중"
                binding.wearStatus.setTextColor(Color.parseColor("#8ECCFC"))
            } else {
                binding.wearStatus.text = "착용 대기 중"
                binding.wearStatus.setTextColor(Color.parseColor("#919191"))
            }
        }

        bluetoothViewModel.wearingFlag.observe(requireActivity()) {
            viewModel.setDailyWearingTime(1000 * 20L)
            // 일일 착용 시간은 DailyWearingTime 변수와 바인딩 된게 아니라 getDailyWearingTimeToString()과 매핑 되어 있으므로
            // 옵저버로 감지 불가
            binding.wearTime.text = viewModel.dailyWearingTimeToString
        }
        return view
    }

    // METHOD : 교정 진행률 프로그래스바 초기화 메소드
    private fun setCalibrationProgress(treatmentDays: Long?, curedInfo: CuredInfo?) {
        if (Objects.isNull(treatmentDays) || Objects.isNull(curedInfo)) return

        // 교정 진행 률 (환자 치료 기간 / 완치자의 총 치료 기간 * 100)
        val progress =
            (treatmentDays!! / curedInfo!!.totalTreatmentDate.toDouble() * 100).roundToInt().toDouble()

        // 교정 진행률 설정
        viewModel.calibrationProgress.value = progress
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