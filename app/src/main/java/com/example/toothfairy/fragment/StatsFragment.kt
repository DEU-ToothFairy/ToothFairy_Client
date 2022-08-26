package com.example.toothfairy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.toothfairy.R
import com.example.toothfairy.adapter.GraphAdapter
import com.example.toothfairy.databinding.FragmentStatsBinding
import java.util.*
import kotlin.math.abs


/**
 * A simple [Fragment] subclass.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    // VARIABLE
    lateinit var binding: FragmentStatsBinding
    lateinit var rvGraph:RecyclerView
    lateinit var graphAdapter: GraphAdapter

    lateinit var data:MutableList<Float>
    var week:Int = 7        // 가로 7일
    var hour:Int = 24       // 세로 24시간
    var lineWidth:Int = 30  // 라인 크기 30px

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    // Activity의 onCreate와 동일한 역할
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // 데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)

        val view = binding.root
        rvGraph = binding.rvGraph

        data = sampleData()

        var linearLayoutManager:LinearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager.stackFromEnd = true

        graphAdapter = GraphAdapter(data)
        graphAdapter.widthCount = week
        graphAdapter.heightCount = hour
        graphAdapter.graphLineWidth = lineWidth

        rvGraph.layoutManager = linearLayoutManager
        rvGraph.adapter = graphAdapter


        rvGraph.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, scrollState: Int ) {
                super.onScrollStateChanged(recyclerView, scrollState)
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    rvGraph.post { autoScroll() }
                }
            }

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        return view
    }

    private fun sampleData():MutableList<Float> {
        var data:MutableList<Float> = mutableListOf()

        for (i in 0..100){
            data.add(i, Math.random().toFloat() * 24)
            Log.i("RANDOM", data[i].toString())
        }

        return data
    }

    private fun autoScroll() {
        lateinit var graph: FrameLayout

        if (data.size > 0) {
            val xy = IntArray(2)
            var gap = 0
            var position = 0
            var minimumGap = -1

            for (i in 0 until rvGraph.childCount) {
                graph = rvGraph.getChildAt(i) as FrameLayout // 리사이클러뷰 안의 막대를 하나씩 가져옴

                if (graph != null) {
                    graph.getLocationInWindow(xy) // 해당 그래프의 절대 좌표 값


                    position = xy[0] + (graph.width + lineWidth) / 2  // (프레임의 넓이 + 막대기의 넓이) / 2 -> 중간으로 설정 됨 + x 좌표
                    gap = position - rvGraph.width
                    Log.i("$i 번 그래프", "x = ${xy[0]} graph.width = ${graph.width} lineWidth = ${lineWidth} position = ${position} gap = ${gap}")

                    // 가장 가까운 그래프까지의 거리 차이를 저장
                    if (minimumGap == -1 || abs(gap) < abs(minimumGap)) {
                        minimumGap = gap
                    }
                }
            }

            Log.i("리사이클러 뷰 넓이", rvGraph.width.toString())
            Log.i("이동 할 넓이", minimumGap.toString())

            rvGraph.smoothScrollBy(minimumGap, 0) // minimumGap 만큼 이동
        }
    }
//    fun initHorizontalCalendar(view: View) {
//        // 시작 날짜 (회원 가입 날짜)
//        val startDate = Calendar.getInstance()
//        startDate.add(Calendar.MONTH, -1)
//
//        // 종료 날짜 (현재 날짜)
//        val endDate = Calendar.getInstance()
//        endDate.add(Calendar.MONTH, 1)
//
//        /*
//            INFO 가로 달력 실행
//
//            range(시작 날짜, 종료 날짜)
//            datesNumberOnScreen(보여질 날짜 개수)
//            onDateSelected(날짜 선택하면 실행되는 메소드)
//         */
//
//        val calView = view.findViewById<View>(R.id.calendarView)
//
//        val horizontalCalendar =
//            HorizontalCalendar.Builder(view /* 액티비티가 아닌 Fragment의 View를 줘야함 */, calView.id)
//                .range(startDate, endDate)
//                .mode(HorizontalCalendar.Mode.DAYS)
//                .datesNumberOnScreen(5)
//                .build()
//
//        // 날짜 설정
//        year = startDate[Calendar.YEAR]
//        month = startDate[Calendar.MONTH] + 1
//        day = startDate[Calendar.DAY_OF_MONTH]
//        calendarTextView!!.text = "선택한 날짜 : " + year + "년 " + month + "월 " + day + "일"
//
//        // 날짜 선택 이벤트
//        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
//            override fun onDateSelected(date: Calendar, position: Int) {
//                year = date[Calendar.YEAR]
//                month = date[Calendar.MONTH] + 1
//                day = date[Calendar.DAY_OF_MONTH]
//                calendarTextView!!.text = "선택한 날짜 : " + year + "년 " + month + "월 " + day + "일"
//            }
//        }
//    }

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
         * @return A new instance of fragment StatsFragment.
         */

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): StatsFragment {
            val fragment = StatsFragment()
            val args = Bundle()

            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)

            fragment.arguments = args
            return fragment
        }
    }
}