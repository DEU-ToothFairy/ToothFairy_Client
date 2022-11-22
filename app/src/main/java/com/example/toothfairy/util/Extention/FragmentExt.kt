package com.example.toothfairy.util.Extention

import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.toothfairy.R
import com.example.toothfairy.view.activity.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


fun Fragment.hideTitleBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            it.findViewById<LinearLayout>(R.id.titleBar).visibility = View.GONE
        }
    }
}

fun Fragment.hideBottomNabBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            it.findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.GONE
        }
    }
}

fun Fragment.showTitleBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            it.findViewById<LinearLayout>(R.id.titleBar).visibility = View.VISIBLE
        }
    }
}

fun Fragment.showBottomNabBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            it.findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.VISIBLE
        }
    }
}

fun Fragment.backToPrevious(){
    requireActivity().supportFragmentManager
        .beginTransaction()
        .remove(this)
        .commit()
    requireActivity().supportFragmentManager.popBackStack();
}