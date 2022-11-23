package com.example.toothfairy.util

import androidx.fragment.app.Fragment
import com.example.toothfairy.view.guideFragment.*

object GuideFactory {
    private class Key(
        private val fragmentName:String = "",
        private val position:Int
    ){
        override fun equals(other: Any?): Boolean {
            if(this === other) return true
            if(other !is Key) return false

            val key:Key = other

            return this.fragmentName == key.fragmentName && this.position == key.position
        }

        override fun hashCode(): Int {
            val result = fragmentName.hashCode()
            return 31 * result + (position.hashCode())
        }
    }

    private val map:MutableMap<Key, Fragment> = mutableMapOf()

    init {
        map[Key("Facial", 0)] = FacialGuide1Fragment()
        map[Key("Facial", 1)] = FacialGuide2Fragment()
        map[Key("Facial", 2)] = FacialGuide3Fragment()

        map[Key("Protruding", 0)] = ProtrudingGuide1Fragment()
        map[Key("Protruding", 1)] = ProtrudingGuide2Fragment()
        map[Key("Protruding", 2)] = ProtrudingGuide3Fragment()

        map[Key("Toothbrush", 0)] = ToothBrushGuide1Fragment()
        map[Key("Toothbrush", 1)] = ToothBrushGuide2Fragment()

    }

    fun getGuideFragment(guideType:String, position:Int): Fragment{
        return map[Key(guideType, position)]!!
    }
}