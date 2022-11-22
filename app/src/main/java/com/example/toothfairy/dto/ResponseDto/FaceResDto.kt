package com.example.toothfairy.dto.ResponseDto

class FaceResDto {
    data class DetectResult(
        var result:Float,
        var type:String
    )
}