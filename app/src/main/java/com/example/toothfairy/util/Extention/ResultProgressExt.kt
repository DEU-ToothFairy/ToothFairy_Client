package com.example.toothfairy.util.Extention

import android.view.View
import com.example.toothfairy.R
import com.example.toothfairy.application.MyApplication
import com.example.toothfairy.databinding.SubCenterStartProgressbarBinding
import kotlin.math.abs


fun SubCenterStartProgressbarBinding.setProgress(progress:Int){
    /**
     * 프로그레스의 최솟 값을 10으로 지정하기 위한 코드
     * 음수의 경우 절댓값을 취해서 비교하면 모두 양수가 되어버리므로,
     * progress / abs(progress)를 통해 progress의 부호를 적용
     */
    if (progress == 0){
        this.leftProgressLayout.visibility = View.VISIBLE
        this.rightProgressLayout.visibility = View.INVISIBLE

        leftProgress.progress = 5
        leftProgressText.text = ""

        return
    }

    val tempProgress = if(abs(progress) < 10) 10 * (progress / abs(progress)) else progress

    if(tempProgress < 0){
        this.leftProgressLayout.visibility = View.VISIBLE
        this.rightProgressLayout.visibility = View.INVISIBLE

        leftProgress.progress = abs(tempProgress)
        leftProgressText.text = "${tempProgress}%"
        MyApplication.ApplicationContext()?.let {
            leftText.setTextColor(it.getColor(R.color.colorAccent))
            rightText.setTextColor(it.getColor(R.color.text_black_gray))
        }
    } else {
        this.leftProgressLayout.visibility = View.INVISIBLE
        this.rightProgressLayout.visibility = View.VISIBLE

        rightProgress.progress = tempProgress
        rightProgressText.text = "${tempProgress}%"
        MyApplication.ApplicationContext()?.let {
            rightText.setTextColor(it.getColor(R.color.colorAccent))
            leftText.setTextColor(it.getColor(R.color.text_black_gray))
        }

    }
}

fun SubCenterStartProgressbarBinding.setMax(max:Int){
    this.rightProgress.max  = max
    this.leftProgress.max   = max
}

fun SubCenterStartProgressbarBinding.setMin(min:Int){
    this.rightProgress.min  = min
    this.leftProgress.min   = min
}

fun SubCenterStartProgressbarBinding.setLeftText(text:String){
    this.leftText.text = text
}

fun SubCenterStartProgressbarBinding.setRightText(text:String){
    this.rightText.text = text
}