package com.example.toothfairy.fragment;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.toothfairy.MainActivity;
import com.example.toothfairy.R;
import com.example.toothfairy.data.WearingStats;
import com.example.toothfairy.databinding.FragmentHomeBinding;
import com.example.toothfairy.entity.CuredInfo;
import com.example.toothfairy.entity.Patient;
import com.example.toothfairy.util.DateManager;
import com.example.toothfairy.viewModel.BluetoothViewModel;
import com.example.toothfairy.viewModel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // 싱글턴 패턴
    public static HomeFragment homeFragment = new HomeFragment();
    public static HomeFragment getInstance(){ return homeFragment; }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // VARIABLE
    FragmentHomeBinding binding; // DataBinding
    MainViewModel viewModel;
    BluetoothViewModel bluetoothViewModel;
    SQLiteDatabase db;

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
        // View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        bluetoothViewModel = BluetoothViewModel.getInstance();
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    
        // 데이터를 관리하는 뷰 모델을 binding에 연결해줘야 적용 됨
        binding.setLifecycleOwner(requireActivity());
        binding.setHomeViewModel(viewModel);

        // 오늘 날짜 설정
        viewModel.getToday().setValue(DateManager.getToday());

        MutableLiveData<Long> treatmentDays = viewModel.getTreatmentDays();
        MutableLiveData<CuredInfo> curedInfoMutableLiveData = viewModel.getCuredInfo();

        // 환자 정보가 갱신 된 경우
        viewModel.getPatient().observe(requireActivity(), new Observer<Patient>() {
            @Override
            public void onChanged(Patient patient) {
                // 치료 기간 설정 (현재 날짜 - 치료 시작 날짜)
                treatmentDays.setValue(DateManager.getElapsedDate(patient.getStartDate()));

                setCalibrationProgress(treatmentDays.getValue(), curedInfoMutableLiveData.getValue());
            }
        });

        // 완치자 정보가 갱신 된 경우
        viewModel.getCuredInfo().observe(requireActivity(), new Observer<CuredInfo>() {
            @Override
            public void onChanged(CuredInfo curedInfo) {
                setCalibrationProgress(treatmentDays.getValue(), curedInfo);
            }
        });

        bluetoothViewModel.getWearStatus().observe(requireActivity(), (status)->{
            if(status){
                binding.wearStatus.setText("현재 착용 중");
                binding.wearStatus.setTextColor(Color.parseColor("#8ECCFC"));
            }
            else {
                binding.wearStatus.setText("착용 대기 중");
                binding.wearStatus.setTextColor(Color.parseColor("#919191"));
            }
        });

        bluetoothViewModel.getWearingFlag().observe(requireActivity(), (flag)->{
            viewModel.setDailyWearingTime(1000 * 20L);
            // 일일 착용 시간은 DailyWearingTime 변수와 바인딩 된게 아니라 getDailyWearingTimeToString()과 매핑 되어 있으므로
            // 옵저버로 감지 불가
            binding.wearTime.setText(viewModel.getDailyWearingTimeToString());
        });

        return view;
    }


    // METHOD : 교정 진행률 프로그래스바 초기화 메소드
    public void setCalibrationProgress(Long treatmentDays, CuredInfo curedInfo){
        if(Objects.isNull(treatmentDays) || Objects.isNull(curedInfo)) return;

        // 교정 진행 률 (환자 치료 기간 / 완치자의 총 치료 기간 * 100)
        double progress = Math.round((treatmentDays / (double)curedInfo.getTotalTreatmentDate()) * 100);

        // 교정 진행률 설정
        viewModel.getCalibrationProgress().setValue(progress);

    }
}