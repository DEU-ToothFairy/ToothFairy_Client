package com.example.toothfairy.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.toothfairy.util.GuideFactory

class GuideAdapter(fragment: Fragment, private val guideType:String, private val count:Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = count

    override fun createFragment(position: Int): Fragment {
        return GuideFactory.getGuideFragment(guideType, position)
    }

}