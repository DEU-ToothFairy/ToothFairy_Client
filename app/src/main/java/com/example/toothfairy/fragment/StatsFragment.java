package com.example.toothfairy.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toothfairy.R;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView calendarTextView;
    int year, month, day;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override // Activity의 onCreate와 동일한 역할
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        calendarTextView = view.findViewById(R.id.calendarTextView);

        initHorizontalCalendar(view);

        return view;
    }


    public void initHorizontalCalendar(View view){
        // 시작 날짜 (회원 가입 날짜)
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        // 종료 날짜 (현재 날짜)
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        /*
            INFO 가로 달력 실행

            range(시작 날짜, 종료 날짜)
            datesNumberOnScreen(보여질 날짜 개수)
            onDateSelected(날짜 선택하면 실행되는 메소드)
         */

        View calView = view.findViewById(R.id.calendarView);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view /* 액티비티가 아닌 Fragment의 View를 줘야함 */, calView.getId())
                .range(startDate, endDate)
                .mode(HorizontalCalendar.Mode.DAYS)
                .datesNumberOnScreen(5)
                .build();

        // 날짜 설정
        year  = startDate.get(Calendar.YEAR);
        month = startDate.get(Calendar.MONTH) + 1;
        day   = startDate.get(Calendar.DAY_OF_MONTH);

        calendarTextView.setText("선택한 날짜 : " + year + "년 " + month + "월 " + day + "일");

        // 날짜 선택 이벤트
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                year = date.get(Calendar.YEAR);
                month = date.get(Calendar.MONTH) + 1;
                day = date.get(Calendar.DAY_OF_MONTH);

                calendarTextView.setText("선택한 날짜 : " + year + "년 " + month + "월 " + day + "일");
            }
        });
    }
}