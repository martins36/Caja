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
        return "$ $price"
    }
}