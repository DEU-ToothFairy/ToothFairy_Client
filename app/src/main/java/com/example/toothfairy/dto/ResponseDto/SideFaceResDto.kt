package com.example.toothfairy.dto.ResponseDto

import com.google.gson.annotations.SerializedName

class SideFaceResDto {
    data class DetectResult(
        @SerializedName("nose")     val nose    : String,
        @SerializedName("chin")     val chin    : String,
        @SerializedName("score")    val score   : Double,
        @SerializedName("dots_num") val dots    : Int
    )

}