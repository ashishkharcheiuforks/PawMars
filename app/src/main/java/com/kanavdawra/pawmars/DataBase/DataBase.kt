package com.kanavdawra.pawmars.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context):SQLiteOpenHelper(context, "PawMars",null,1) {
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("CREATE TABLE contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, contact_id VARCHAR(64), name VARCHAR(255), phno VARCHAR(5000), email VARCHAR(20000), photo BOOL, sync INTEGER)")
        database?.execSQL("CREATE TABLE contactList (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255))")

    }
}