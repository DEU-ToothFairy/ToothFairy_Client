package com.example.toothfairy.dto.ResponseDto

import com.example.toothfairy.data.FaceType

class FaceResDto {
    data class DetectResult(
        var result:Double,
        var type:String,
        val eyeDegree: Double,
        val lipDegree: Double
    )
}