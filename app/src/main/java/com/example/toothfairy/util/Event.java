package com.example.toothfairy.util;

public class Event<T> {
    private boolean hasBeenHandled = false;
    private T content;

    public Event(T content) { this.content = content; }

    public T getContentIfNotHandled(){
        if(hasBeenHandled) return null;

        hasBeenHandled = true;
        return content;
    }

    public void setContent(T content){ this.content = content; }
}
