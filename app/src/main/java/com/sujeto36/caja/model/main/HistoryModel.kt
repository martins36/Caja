package com.sujeto36.caja.model.main

class HistoryModel (
    val dateSueldo: String,
    val historyChildModel: ArrayList<HistoryChildModel>,
    private val total: Int) {

    fun totalFormat(): String {
        var str = ""
        val num = total.toString()
        for (i in 0.until(num.length)) {
            str += num[num.length - (i + 1)]
            if ((i + 1)%3 == 0 && i != num.length - 1) {
                str += "."
            }
        }
        return "$ ${str.reversed()}"
    }
}