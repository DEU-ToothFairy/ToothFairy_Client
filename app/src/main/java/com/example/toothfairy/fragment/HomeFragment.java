package com.example.toothfairy.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.toothfairy.R;
import com.example.toothfairy.databinding.FragmentHomeBinding;
import com.example.toothfairy.entity.Patient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MutableLiveData<Patient> liveDataPatient = new MutableLiveData<>();


    // DEFINE View 인스턴스 선언
    ProgressBar profileProgressBar, wearProgressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        FragmentHomeBinding fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        setProfileProgressBar(60, view);
        setWearProgressBar(60, view);
        setAvgProgressBar(53, 60, view);
        setMaxProgressBar(60, 70, view);
        setMinProgressBar(43, 52, view);

        return view;
    }


    // METHOD : 평균 착용 시간 프로그래스바 초기화 메소드
    public void setAvgProgressBar(int patientProgress, int curedProgress, View view){
        ProgressBar userAvg = view.findViewById(R.id.userWearAvgProgressBar);
        ProgressBar curedAvg = view.findViewById(R.id.curedWearAvgProgressBar);

        userAvg.setProgress(patientProgress);
        userAvg.setMax(100);

        curedAvg.setProgress(curedProgress);
        curedAvg.setMax(100);
    }

    // METHOD : 최대 착용 시간 프로그래스바 초기화 메소드
    public void setMaxProgressBar(int patientProgress, int curedProgress, View view){
        ProgressBar userMax = view.findViewById(R.id.userWearMaxProgressBar);
        ProgressBar curedMax = view.findViewById(R.id.curedWearMaxProgressBar);

        userMax.setProgress(patientProgress);
        userMax.setMax(100);

        curedMax.setProgress(curedProgress);
        curedMax.setMax(100);
    }

    // METHOD : 최소 착용 시간 프로그래스바 초기화 메소드
    public void setMinProgressBar(int patientProgress, int curedProgress, View view){
        ProgressBar userMin = view.findViewById(R.id.userWearMinProgressBar);
        ProgressBar curedMin = view.findViewById(R.id.curedWearMinProgressBar);

        userMin.setProgress(patientProgress);
        userMin.setMax(100);

        curedMin.setProgress(curedProgress);
        curedMin.setMax(100);
    }

    // Method 프로필 프로그래스바 초기화
    public void setProfileProgressBar(int progress, View view) {
        profileProgressBar = view.findViewById(R.id.profileProgressBar);
        profileProgressBar.setProgress(progress);
    }

    // Method 착용 시간 프로그래스바 초기화
    public void setWearProgressBar(int progress, View view){
        wearProgressBar = view.findViewById(R.id.wearingProgressBar);
        wearProgressBar.setProgress(progress);
    }
}