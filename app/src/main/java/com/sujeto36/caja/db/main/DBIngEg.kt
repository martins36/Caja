package com.sujeto36.caja.db.main

import android.provider.BaseColumns

object DBIngEg {

    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "ING_EG"
            const val COLUMN_DATE = "date"
            const val COLUMN_DESC = "desc"
            const val COLUMN_PRICE = "price"
            const val COLUMN_TYPE = "type"
            const val COLUMN_NUM = "num"
            const val COLUMN_STATUS = "status"
        }
    }
}