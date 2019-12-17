package com.sujeto36.caja

class Util {

    fun priceFormat(price: Int) : String {
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