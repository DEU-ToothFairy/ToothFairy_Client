package com.example.toothfairy.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.toothfairy.R
import com.example.toothfairy.data.ScoreBoard

class ScoreBoardLayout(context: Context) : LinearLayout(context) {
    constructor(context: Context, attrs:AttributeSet, scoreBoard: ScoreBoard) : this(context = context){
        init(context, scoreBoard)
    }

    constructor(context: Context, scoreBoard: ScoreBoard) : this(context = context) {
        init(context, scoreBoard)
    }

    /** scoreBoard를 받아서 scoreBoard 값에 해당하는 레이아웃을 추가하는 메소드 */
    private fun init(context: Context, scoreBoard: ScoreBoard){
        val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // score_board_layout을 인플레이션(메모리에 등록)
        inflater.inflate(R.layout.sub_score_board_layout, this, true)

        var scoreBoard:CardView = findViewById(R.id.scoreboard)
    }
}