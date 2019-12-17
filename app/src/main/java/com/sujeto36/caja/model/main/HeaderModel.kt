package com.sujeto36.caja.model.main

import com.sujeto36.caja.Util

class HeaderModel (
    var date: String,
    var price: Int) {

    constructor(date: String) : this (date, 0)

    fun priceFormat() : String {
        val util = Util()
        return util.priceFormat(price)
    }

    fun dateShort() : String {
        return date.subSequence(0, 5).toString()
    }
}