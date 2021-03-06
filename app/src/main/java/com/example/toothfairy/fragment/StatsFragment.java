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

    @Override // Activity??? onCreate??? ????????? ??????
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        calendarTextView = view.findViewById(R.id.calendarTextView);

        initHorizontalCalendar(view);

        return view;
    }


    public void initHorizontalCalendar(View view){
        // ?????? ?????? (?????? ?????? ??????)
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        // ?????? ?????? (?????? ??????)
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        /*
            INFO ?????? ?????? ??????

            range(?????? ??????, ?????? ??????)
            datesNumberOnScreen(????????? ?????? ??????)
            onDateSelected(?????? ???????????? ???????????? ?????????)
         */

        View calView = view.findViewById(R.id.calendarView);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view /* ??????????????? ?????? Fragment??? View??? ????????? */, calView.getId())
                .range(startDate, endDate)
                .mode(HorizontalCalendar.Mode.DAYS)
                .datesNumberOnScreen(5)
                .build();

        // ?????? ??????
        year  = startDate.get(Calendar.YEAR);
        month = startDate.get(Calendar.MONTH) + 1;
        day   = startDate.get(Calendar.DAY_OF_MONTH);

        calendarTextView.setText("????????? ?????? : " + year + "??? " + month + "??? " + day + "???");

        // ?????? ?????? ?????????
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                year = date.get(Calendar.YEAR);
                month = date.get(Calendar.MONTH) + 1;
                day = date.get(Calendar.DAY_OF_MONTH);

                calendarTextView.setText("????????? ?????? : " + year + "??? " + month + "??? " + day + "???");
            }
        });
    }
}