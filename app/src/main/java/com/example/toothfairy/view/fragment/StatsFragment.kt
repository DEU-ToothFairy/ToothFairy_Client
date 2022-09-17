package com.example.toothfairy.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.toothfairy.R
import com.example.toothfairy.adapter.CalendarAdapter
import com.example.toothfairy.data.CalendarDate
import com.example.toothfairy.databinding.FragmentStatsBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
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
    lateinit var bind: FragmentStatsBinding
    lateinit var layoutManager: LinearLayoutManager

    val itemList = arrayListOf<CalendarDate>()
    val calendarAdapter = CalendarAdapter(itemList)
    var selectorPosition:Int = itemList.size - 1

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)
        val view = bind.root

//        // 데이터를 관리하는 뷰 모델을 binding에 연결해줘야 적용 됨
//        binding.lifecycleOwner = requireActivity()

        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        bind.recyclerView.layoutManager = layoutManager

        initRecyclerView()
        calendarEventAdder()

        // todayTv Spannable로 폰트 스타일 변경
        styleChangeTodayTv()
        // userScoreTv Spannable로 폰트 스타일 변경
        styleChangeUserScoreTV()

        return view
    }

    /** 사용자 교정 점수 Textview 스타일 변경 */
    private fun styleChangeUserScoreTV(){
        val userScore:String = bind.userScoreTv.text.toString()
        val builder = SpannableStringBuilder(userScore)

        val colorBlueSpan = ForegroundColorSpan(resources.getColor(R.color.colorAccent))
        builder.setSpan(colorBlueSpan, userScore.length - 9, userScore.length - 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        bind.userScoreTv.text = builder
    }

    /** 선택 날짜 Textview 스타일 변경 */
    private fun styleChangeTodayTv(){
        val selectDate:String = bind.todayTv.text.toString()
        val builder = SpannableStringBuilder(selectDate)

        builder.setSpan(StyleSpan(Typeface.BOLD), selectDate.length - 3, selectDate.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val sizeBigSpan = RelativeSizeSpan(0.8f)
        builder.setSpan(sizeBigSpan, selectDate.length - 3, selectDate.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        bind.todayTv.text = builder
    }

    /** 리사이클러뷰 초기화 */
    private fun initRecyclerView(){
        // 현재 달의 마지막 날짜
        val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
        lastDayOfMonth.format(DateTimeFormatter.ofPattern("dd"))

        val today = LocalDate.now()

        /* NEED FIX 교정 시작일 부터 현재까지로 바꿔야함 */
        for(i in 1..today.monthValue){ // 1월부터 12월 까지
            for (j in 1..YearMonth.of(today.year, i).lengthOfMonth()){ // 해당 년도, 해당 월의 마지막 달까지 반복
                val localDate = LocalDate.of(today.year, i, j)
                val dayOfWeek: DayOfWeek = localDate.dayOfWeek // MONDAY, TUESDAY 같은 요일의 이름을 가져옴

                itemList.add(CalendarDate(
                        dayOfWeek.toString().substring(0,1), // 요일
                        localDate // 날짜 YYYY-MM-DD
                    )
                )
            }
        }

        // RecyclerView Item 클릭 이벤트 리스너 등록
        // calendarAdapter 내에 onItemClickListener 인터페이스를 선언
        // 외부에서 리스너 인터페이스를 구현할 수 있게하여 좀 더 다양한 작업이 가능하게 함
        calendarAdapter.onItemClickListener = object : CalendarAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                val item:CalendarDate = itemList[position]

                bind.selector.x = view.x + (view.paddingLeft / 2)

                // selectorPosition은 완전히 보이는 아이템의 Position으로부터 셀렉터가 얼만큼 떨어져있는지를 저장해둠
                selectorPosition = position - (bind.recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                bind.selectDateTv.text = "${item.date.year}년 ${item.date.monthValue}월 ${item.date.dayOfMonth}일"
            }
        }

        bind.recyclerView.adapter = calendarAdapter
        bind.recyclerView.scrollToPosition(itemList.size - 1)
    }

    /** 캘린더 리사이클러뷰 이벤트 */
    private fun calendarEventAdder(){
        bind.recyclerView.addOnScrollListener(object : OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    bind.recyclerView.post{ autoScroll() }


                    // 완전히 보이는 아이템의 Position + SelectorPosition
                    val item = itemList[
                            (bind.recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() + selectorPosition]

                    bind.selectDateTv.text = "${item.date.year}년 ${item.date.monthValue}월 ${item.date.dayOfMonth}일"
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun autoScroll() {
        lateinit var date: LinearLayout
        val calendar = bind.recyclerView

        val xy = IntArray(2)
        var gap = 0
        var position = 0
        var minimumGap = -1

        for (i in 0 until calendar.childCount) {
            date = calendar.getChildAt(i) as LinearLayout // 리사이클러뷰 안의 날짜를 하나씩 가져옴

            if (date != null) {
                date.getLocationInWindow(xy) // 해당 날짜의 절대 좌표 값

                position = xy[0] + (date.width) / 4  // (프레임의 넓이 + 날짜 뷰의 넓이) / 2 -> 중간으로 설정 됨 + x 좌표
                gap = position - calendar.width

                // 가장 가까운 그래프까지의 거리 차이를 저장
                if (minimumGap == -1 || abs(gap) < abs(minimumGap)) {
                    minimumGap = gap
                }
            }
        }

        calendar.smoothScrollBy(minimumGap, 0) // minimumGap 만큼 이동
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