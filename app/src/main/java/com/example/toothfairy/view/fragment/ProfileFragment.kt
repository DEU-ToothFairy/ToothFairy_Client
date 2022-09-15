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
import androidx.fragment.app.Fragment
import com.example.toothfairy.view.activity.LoginActivity
import com.example.toothfairy.databinding.FragmentProfileBinding

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
    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        val view = binding?.root

        // Inflate the layout for this fragment

        // 로그아웃 클릭 이벤트
        binding?.logoutBtn?.setOnClickListener { v: View? ->
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
        return view
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