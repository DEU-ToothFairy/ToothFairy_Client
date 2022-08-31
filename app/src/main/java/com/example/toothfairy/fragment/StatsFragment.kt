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
import com.example.toothfairy.adapter.CalendarAdapter
import com.example.toothfairy.adapter.GraphAdapter
import com.example.toothfairy.data.CalendarDate
import com.example.toothfairy.databinding.FragmentStatsBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
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
    val itemList = arrayListOf<CalendarDate>()
    val calendarAdapter = CalendarAdapter(itemList)
    lateinit var layoutManager: LinearLayoutManager

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

//        // 데이터를 관리하는 뷰 모델을 binding에 연결해줘야 적용 됨
//        binding.lifecycleOwner = requireActivity()

        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.calendarView.layoutManager = layoutManager

        setListView()

        return view
    }

    private fun setListView(){
        // 현재 달의 마지막 날짜
        val yearMonth = YearMonth.of(2022, 1)

        val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
        lastDayOfMonth.format(DateTimeFormatter.ofPattern("dd"))

        val year = LocalDate.now().year

        for(i in 1..12){ // 1월부터 12월 까지
            for (j in 1..YearMonth.of(year, i).lengthOfMonth()){ // 해당 년도, 해당 월의 마지막 달까지 반복
                val localDate = LocalDate.of(year, i, j)
                val dayOfWeek: DayOfWeek = localDate.dayOfWeek // MONDAY, TUESDAY 같은 요일의 이름을 가져옴

                itemList.add(CalendarDate(dayOfWeek.toString().substring(0,1), j.toString()))
            }
        }


        binding.calendarView.adapter = calendarAdapter
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