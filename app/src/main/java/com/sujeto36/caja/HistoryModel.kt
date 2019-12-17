package com.sujeto36.caja

class HistoryModel (
    val dateSueldo: String,
    val historyChildModel: ArrayList<HistoryChildModel>,
    private val total: Int) {

    fun totalFormat(): String {
        return "$ $total"
    }
}