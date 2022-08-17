package com.example.toothfairy.util

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