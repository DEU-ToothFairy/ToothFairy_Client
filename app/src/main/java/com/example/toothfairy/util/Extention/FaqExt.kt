package com.example.toothfairy.util.Extention

import com.example.toothfairy.data.Faq
import com.example.toothfairy.dto.ResponseDto.FaqResDto


fun FaqResDto.Faq.getFaqList(): ArrayList<Faq>{
    val faqlist = arrayListOf<Faq>()
    for(i in 0 until this.question.size){
        faqlist.add(
            Faq(
                question = question[i],
                answer = answer[i]
            )
        )
    }

    return faqlist
}