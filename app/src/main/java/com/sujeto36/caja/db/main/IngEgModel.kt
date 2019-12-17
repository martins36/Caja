package com.sujeto36.caja.db.main

class IngEgModel (
    val date: String,
    val desc: String,
    val price: Int,
    val type: String,
    val num: Int,
    val status: Int) {

    constructor(date: String, desc: String, price: Int) :
            this(date, desc, price, "", -1, 1)

    fun priceFormat() : String {
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