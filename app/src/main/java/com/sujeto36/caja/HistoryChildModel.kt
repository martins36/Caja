package com.sujeto36.caja

class HistoryChildModel (
    private val date: String,
    private val desc: String,
    private val price: Int,
    val type: String) {

    fun descFormat(): String {
        return "${date.subSequence(0, 5)} - $desc"
    }

    fun priceFormat(): String {
        return "$ $price"
    }
}