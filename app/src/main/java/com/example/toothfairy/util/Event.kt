package com.example.toothfairy.util

/** 중복으로 이벤트가 발생하는 것을 막기위한 클래스 */
class Event<T>(private var content: T) {
    private var hasBeenHandled = false

    fun contentIfNotHandled(): T?{
        if(hasBeenHandled) return null

        hasBeenHandled = true
        return content
    }

    fun setContent(content: T) {
        this.content = content
    }
}