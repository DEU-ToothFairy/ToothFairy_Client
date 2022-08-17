package com.example.toothfairy.data

class WearingTime {
    var start: Long? = null  // milliseconds
    var end: Long? = null // milliseconds

    constructor(start: Long?, end: Long?) {
        this.start = start
        this.end = end
    }

    constructor() {}

    val wearingTime: Long
        get() = end!! - start!!
    val hour: Int
        get() = ((end!! - start!!) / (1000 * 60)).toInt() / 60
    val minute: Int
        get() = ((end!! - start!!) / (1000 * 60)).toInt() % 60

    override fun toString(): String {
        return "WearingTime(start=$start, end=$end)"
    }
}