package com.example.toothfairy.view.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.toothfairy.R
import com.example.toothfairy.data.ScoreBoard

class ScoreBoardLayout(context: Context, scoreBoard: ScoreBoard) : LinearLayout(context) {

    init {
        init(context, scoreBoard)
    }

    private fun init(context: Context, scoreBoard: ScoreBoard){
        val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        inflater.inflate(R.layout.score_board_layout, this, true)


    }
}