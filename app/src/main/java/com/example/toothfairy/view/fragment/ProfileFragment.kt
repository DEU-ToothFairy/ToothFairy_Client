package com.example.toothfairy.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.toothfairy.R
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.view.activity.LoginActivity
import com.example.toothfairy.databinding.FragmentProfileBinding
import com.example.toothfairy.viewModel.MainViewModel
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    
    // 데이터 바인딩 변수
    private lateinit var bind: FragmentProfileBinding
    private lateinit var mainVM: MainViewModel
    private lateinit var radarChart: RadarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        bind.lifecycleOwner = requireActivity()
        bind.mainVM = mainVM

        addLogoutBtnEvent()
        makeRadarChart()

        bind.faqBtn.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, FaqFragment())
                .addToBackStack(null)
                .commit()
        })

        return bind.root
    }

    private fun addLogoutBtnEvent(){
        // 로그아웃 클릭 이벤트
        bind?.logoutBtn?.setOnClickListener { v: View? ->
            // autuLogin 이름의 Preferences를 가져옴
            val prefs = this.requireActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE)

            // 저장 되어있던 id, password 삭제
            val editor = prefs.edit()
            editor.remove("id")
            editor.remove("password")
            editor.commit()

            ActivityCompat.finishAffinity(this.requireActivity())

            val intent = Intent(this.requireActivity().applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)
        }
    }

    private fun makeRadarChart() {
        radarChart = bind.radarChart

        radarChart.apply {
            webColor = resources.getColor(R.color.light_gray)
            webColorInner = resources.getColor(R.color.light_gray)
            webLineWidth = 1f
            webLineWidthInner = 1f
            isRotationEnabled = false       // 회전 안되게 설정
            isClickable = false
            onTouchListener = null
            description = null
            legend.isEnabled = false        // description label, Data 라벨 제거
        }

        var dataSet: RadarDataSet = RadarDataSet(radarChatDatas(), "DATA")

        dataSet.apply {
            setDrawFilled(true)                                  // 데이터 그래프 색으로 채울지 설정
            color = resources.getColor(R.color.colorAccent)      // 라인 색상
            fillColor = resources.getColor(R.color.colorAccent)  // 내부 색상
            setDrawValues(false)                                 // 그래프 밸류 값 표시 설정
            lineWidth = 2f                                       // 데이터 그래프 선 굵기 설정
        }

        var data: RadarData = RadarData()
        data.addDataSet(dataSet)

        val labels: Array<String> = arrayOf("착용 시간", "착용 점수", "체크리스트", "실천") // 라벨 설정

        radarChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textSize = 12f
        }

        radarChart.yAxis.apply {
            setDrawLabels(false)
            axisMaximum = 10f
            axisMinimum = 0f
            setLabelCount(4,false) // 그래프 라인 갯수
        }
        radarChart.data = data;

    }

    private fun radarChatDatas(): ArrayList<RadarEntry> {
        var datas: ArrayList<RadarEntry> = arrayListOf()

        datas.add(RadarEntry(8f))
        datas.add(RadarEntry(10f))
        datas.add(RadarEntry(6f))
        datas.add(RadarEntry(7f))

        return  datas;
    }

    companion object {
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()

            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)

            fragment.arguments = args
            return fragment
        }
    }
}