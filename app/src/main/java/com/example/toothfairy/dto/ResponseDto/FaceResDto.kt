package com.example.toothfairy.dto.ResponseDto

import com.example.toothfairy.data.FaceType

class FaceResDto {
    data class DetectResult(
        var result:Double,
        var type:String,
        val eyeSlope : Int,
        val eyeDegree: Double,
        val lipSlope : Int,
        val lipDegree: Double,
    )
}