package com.njves.schoolnetwork.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDB(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        //db!!.execSQL("CREATE TABLE $DB_NAME(`id` integer primary key, )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE $TASK_TABLE")
        onCreate(db)
    }
}
const val DB_NAME = "plannerDB"
const val TASK_TABLE = "task"
const val DB_VERSION = 1