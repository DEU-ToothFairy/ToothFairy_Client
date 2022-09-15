package com.example.toothfairy.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentReportBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
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
        return "${value.toInt()} h"
    }
}

class ReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding:FragmentReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        val view = binding?.root

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
                axisMaximum = 25f               // Y축 최대값
                axisMinimum = 0f                // Y축 최소값 0f
                granularity = 6f                // granularity(세분성) 50 단위마다 선을 그림
                // 위 설정이 20f 였으면 총 5개의 선이 그려짐

                valueFormatter = TimeAxisValueFormatter()
                setDrawLabels(false)             // 값 표시(0, 50, 100)
                setDrawAxisLine(false)          // 축 그리기 설정

                setDrawGridLines(false)          // 격자 라인 활용
                enableGridDashedLine(10f, 10f, 0f)

                axisLineColor = ContextCompat.getColor(context, R.color.colorAccent)    // 축 색깔 설정
                gridColor = ContextCompat.getColor(context, R.color.semi_gray)          // 격자 색깔 설정
                textColor = ContextCompat.getColor(context, R.color.blue_gray)               // 라벨 텍스트 컬러 설정
                textSize = 12f                                                          // 라벨 텍스트 크기
            }

            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM       // X 축을 아래에 둠
                granularity = 1f                            // 간격을 1로 설정

                setDrawAxisLine(false)                       // 축 그림
                setDrawGridLines(false)                     // 격자

                valueFormatter = MyXAxisFormatter()
                textColor = ContextCompat.getColor(context, R.color.blue_gray)  // 라벨 색상
                textSize = 10f                                             // 텍스트 크기
            }

            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            legend.isEnabled = false //차트 범례 설정
        }

        var set = BarDataSet(entries,"DataSet")//데이터셋 초기화 하기
        set.color = ContextCompat.getColor(requireContext()!!, R.color.colorAccent)

        val dataSet : ArrayList<IBarDataSet> = ArrayList()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}