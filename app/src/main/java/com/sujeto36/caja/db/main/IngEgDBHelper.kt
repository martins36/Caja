package com.sujeto36.caja.db.main

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sujeto36.caja.HistoryChildModel
import com.sujeto36.caja.HistoryModel

class IngEgDBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun insert(ingEg: IngEgModel) {
        val db = writableDatabase
        val values = ContentValues()

        values.put(DBIngEg.UserEntry.COLUMN_DATE, ingEg.date)
        values.put(DBIngEg.UserEntry.COLUMN_DESC, ingEg.desc)
        values.put(DBIngEg.UserEntry.COLUMN_PRICE, ingEg.price)
        values.put(DBIngEg.UserEntry.COLUMN_TYPE, ingEg.type)
        values.put(DBIngEg.UserEntry.COLUMN_STATUS, ingEg.status)

        if (ingEg.type == "S")
            values.put(DBIngEg.UserEntry.COLUMN_NUM, countSueldo() + 1)
        else
            values.put(DBIngEg.UserEntry.COLUMN_NUM, countSueldo())

        db.insert(DBIngEg.UserEntry.TABLE_NAME, null, values)
        db.close()
    }

    @Throws(SQLiteConstraintException::class)
    fun read(type: String) : ArrayList<IngEgModel> { // lee todos los E/I con estado 1
        val db = writableDatabase
        val ingEg = ArrayList<IngEgModel>()
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT " + DBIngEg.UserEntry.COLUMN_DATE + ", "
                    + DBIngEg.UserEntry.COLUMN_DESC + ", " + DBIngEg.UserEntry.COLUMN_PRICE
                    + " FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_STATUS} = 1 AND ${DBIngEg.UserEntry.COLUMN_TYPE} = '"
                    + type + "'", null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var date: String
        var desc: String
        var price: Int

        if (type == "I")
            ingEg.add(readSueldo())

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                date = cursor.getString(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_DATE))
                desc = cursor.getString(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_DESC))
                price = cursor.getInt(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_PRICE))

                ingEg.add(IngEgModel(date, desc, price))
                cursor.moveToNext()
            }
        }

        cursor.close()
        return ingEg
    }

    @Throws(SQLiteConstraintException::class)
    fun readHistory(num: Int) : HistoryModel { // lee los E/I de un tal NUM con estado 0
        val db = writableDatabase
        val historyChild = ArrayList<HistoryChildModel>()
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT "
                    + DBIngEg.UserEntry.COLUMN_DATE + ", " + DBIngEg.UserEntry.COLUMN_DESC + ", "
                    + DBIngEg.UserEntry.COLUMN_PRICE + ", " + DBIngEg.UserEntry.COLUMN_TYPE
                    + " FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_STATUS} = 0 AND ${DBIngEg.UserEntry.COLUMN_NUM} = "
                    + num.toString(), null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var date: String
        var desc: String
        var price: Int
        var type: String

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                date = cursor.getString(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_DATE))
                desc = cursor.getString(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_DESC))
                price = cursor.getInt(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_PRICE))
                type = cursor.getString(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_TYPE))

                historyChild.add(HistoryChildModel(date, desc, price, type))
                cursor.moveToNext()
            }
        }

        cursor.close()
        return HistoryModel(readSueldoHistory(num).date, historyChild, sumTotalHistory(num))
    }

    @Throws(SQLiteConstraintException::class)
    fun countSueldo() : Int { // cuenta la cantidad de sueldos en total (con estado 0 y 1)
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS NUMERO FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_TYPE} = 'S'", null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var count = 0
        if (cursor!!.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("NUMERO"))
        }

        cursor.close()
        return count
    }

    @Throws(SQLiteConstraintException::class)
    fun countIngEg(type: String) : Int { // cuenta la cantidad de I/E con esatdo 1
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS NUMERO FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_STATUS} = 1 AND ${DBIngEg.UserEntry.COLUMN_TYPE} = '"
                    + type + "'", null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var count = 0
        if (cursor!!.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("NUMERO"))
        }
        if (type == "I")
            count += countSueldo1()

        cursor.close()
        return count
    }

    @Throws(SQLiteConstraintException::class)
    fun clear() { // limpia, lol
        val db = writableDatabase
        val selection = DBIngEg.UserEntry.COLUMN_STATUS + " LIKE ?"
        val selectionArgs = arrayOf(1.toString())
        val values = ContentValues()
        values.put(DBIngEg.UserEntry.COLUMN_STATUS, 0)
        db.update(DBIngEg.UserEntry.TABLE_NAME, values, selection, selectionArgs)
        db.close()
    }

    @Throws(SQLiteConstraintException::class)
    fun sumTotal() : Int { // suma I - E de los sueldos con estado 1
        return sumIngEg("I") - sumIngEg("E")
    }



    ////////// FUNCIONES Y METODOS PRIVADOS //////////


    @Throws(SQLiteConstraintException::class)
    private fun sumTotalHistory(num: Int) : Int { // suma I - E de los sueldos con estado 1
        return sumIngEgHistory("I", num) - sumIngEgHistory("E", num)
    }

    @Throws(SQLiteConstraintException::class)
    private fun sumIngEg(type: String) : Int { // suma los E/I con estado 1
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT SUM(" + DBIngEg.UserEntry.COLUMN_PRICE + ") AS NUMERO"
                    + " FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_STATUS} = 1 AND ${DBIngEg.UserEntry.COLUMN_TYPE} = '"
                    + type + "'", null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var total = 0
        if (cursor!!.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("NUMERO"))
        }
        if (type == "I")
            total += readSueldo().price

        cursor.close()
        return total
    }

    @Throws(SQLiteConstraintException::class)
    private fun sumIngEgHistory(type: String, num: Int) : Int { // suma los E/I con estado 0 de cierto NUM
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT SUM(" + DBIngEg.UserEntry.COLUMN_PRICE + ") AS NUMERO"
                    + " FROM " + DBIngEg.UserEntry.TABLE_NAME + " WHERE ${DBIngEg.UserEntry.COLUMN_NUM} = "
                    + num.toString() + " AND ${DBIngEg.UserEntry.COLUMN_TYPE} = '" + type + "'", null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var total = 0
        if (cursor!!.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("NUMERO"))
        }
        if (type == "I")
            total += readSueldoHistory(num).price

        cursor.close()
        return total
    }

    @Throws(SQLiteConstraintException::class)
    private fun readSueldo() : IngEgModel { // lee el sueldo con estado 1
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT " + DBIngEg.UserEntry.COLUMN_DATE + ", " + DBIngEg.UserEntry.COLUMN_PRICE
                    + " FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_STATUS} = 1 AND ${DBIngEg.UserEntry.COLUMN_TYPE} = 'S'",
                null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var date = ""
        var price = 0

        if (cursor!!.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_DATE))
            price = cursor.getInt(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_PRICE))
        }

        cursor.close()
        return IngEgModel(date, "Sueldo", price)
    }

    @Throws(SQLiteConstraintException::class)
    private fun readSueldoHistory(num: Int) : IngEgModel { // lee el sueldo con cierto NUM
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT " + DBIngEg.UserEntry.COLUMN_DATE + ", " + DBIngEg.UserEntry.COLUMN_PRICE
                    + " FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_NUM} = "
                    + num.toString() + " AND ${DBIngEg.UserEntry.COLUMN_TYPE} = 'S'", null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var date = ""
        var price = 0

        if (cursor!!.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_DATE))
            price = cursor.getInt(cursor.getColumnIndex(DBIngEg.UserEntry.COLUMN_PRICE))
        }

        cursor.close()
        return IngEgModel(date, "Sueldo", price)
    }

    @Throws(SQLiteConstraintException::class)
    private fun countSueldo1() : Int { //cuenta si hay un sueldo con estado 1 :V
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS NUMERO FROM " + DBIngEg.UserEntry.TABLE_NAME
                    + " WHERE ${DBIngEg.UserEntry.COLUMN_STATUS} = 1 AND ${DBIngEg.UserEntry.COLUMN_TYPE} = 'S'",
                    null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }

        var count = 0
        if (cursor!!.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("NUMERO"))
        }

        cursor.close()
        return count
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME ="Caja.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBIngEg.UserEntry.TABLE_NAME + " (" +
                    DBIngEg.UserEntry.COLUMN_DATE + " TEXT," +
                    DBIngEg.UserEntry.COLUMN_DESC + " TEXT," +
                    DBIngEg.UserEntry.COLUMN_PRICE + " INTEGER," +
                    DBIngEg.UserEntry.COLUMN_TYPE + " TEXT," +
                    DBIngEg.UserEntry.COLUMN_NUM + " INTEGER," +
                    DBIngEg.UserEntry.COLUMN_STATUS + " INTEGER)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBIngEg.UserEntry.TABLE_NAME
    }
}