package com.example.toothfairy.dto.ResponseDto

class FaqResDto {
    data class Faq(
        val question    : Array<String>,
        val answer      : Array<String>
    )
}