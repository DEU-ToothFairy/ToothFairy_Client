package com.example.toothfairy.fragment

import android.widget.TextView
import android.os.Bundle
import com.example.toothfairy.fragment.StatsFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.toothfairy.R
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.*

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
    var calendarTextView: TextView? = null
    var year = 0
    var month = 0
    var day = 0

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
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        calendarTextView = view.findViewById(R.id.calendarTextView)

        initHorizontalCalendar(view)
        return view
    }

    fun initHorizontalCalendar(view: View) {
        // 시작 날짜 (회원 가입 날짜)
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        // 종료 날짜 (현재 날짜)
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        /*
            INFO 가로 달력 실행

            range(시작 날짜, 종료 날짜)
            datesNumberOnScreen(보여질 날짜 개수)
            onDateSelected(날짜 선택하면 실행되는 메소드)
         */

        val calView = view.findViewById<View>(R.id.calendarView)

        val horizontalCalendar =
            HorizontalCalendar.Builder(view /* 액티비티가 아닌 Fragment의 View를 줘야함 */, calView.id)
                .range(startDate, endDate)
                .mode(HorizontalCalendar.Mode.DAYS)
                .datesNumberOnScreen(5)
                .build()

        // 날짜 설정
        year = startDate[Calendar.YEAR]
        month = startDate[Calendar.MONTH] + 1
        day = startDate[Calendar.DAY_OF_MONTH]
        calendarTextView!!.text = "선택한 날짜 : " + year + "년 " + month + "월 " + day + "일"

        // 날짜 선택 이벤트
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                year = date[Calendar.YEAR]
                month = date[Calendar.MONTH] + 1
                day = date[Calendar.DAY_OF_MONTH]
                calendarTextView!!.text = "선택한 날짜 : " + year + "년 " + month + "월 " + day + "일"
            }
        }
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