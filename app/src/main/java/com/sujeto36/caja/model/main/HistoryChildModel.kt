package com.sujeto36.caja.model.main

import com.sujeto36.caja.Util

class HistoryChildModel (
    private val date: String,
    private val desc: String,
    private val price: Int,
    val type: String) {

    fun descFormat(): String {
        return "${date.subSequence(0, 5)} - $desc"
    }

    fun priceFormat(): String {
        var str = ""
        val num = price.toString()
        for (i in 0.until(num.length)) {
            str += num[num.length - (i + 1)]
            if ((i + 1)%3 == 0 && i != num.length - 1) {
                str += "."
            }
        }
        return "$ ${str.reversed()}"
    }
}