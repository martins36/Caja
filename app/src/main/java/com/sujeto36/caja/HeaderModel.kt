package com.sujeto36.caja

class HeaderModel (
    var date: String,
    var price: Int) {

    constructor(date: String) : this (date, 0)

    fun priceFormat() : String {
        return "$ $price"
    }

    fun dateShort() : String {
        return date.subSequence(0, 5).toString()
    }
}