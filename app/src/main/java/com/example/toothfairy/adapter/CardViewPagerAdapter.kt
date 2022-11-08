package com.example.toothfairy.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.toothfairy.view.fragment.CardAsymmetryFragment
import com.example.toothfairy.view.fragment.CardProtrudingMouthFragment
import com.example.toothfairy.view.fragment.CardToothbrushFragment

class CardViewPagerAdapter(fragment: Fragment, var count:Int) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {

        return when(getRealPosition(position)){
            0 -> CardAsymmetryFragment()
            1 -> CardProtrudingMouthFragment()
            else -> CardToothbrushFragment()
        }
    }

    private fun getRealPosition(position: Int) = position % count
}