package com.njves.schoolnetwork.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
/*
 * База данных для хранения задач
 * Просмотр задач без интернета
 */
class LocalTaskDb(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TASK_TABLE(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` text not null, `description` text not null,`date` INTEGER, sender text not null, receiver text not null, uid text not null, status integer DEFAULT 0 not null)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE $TASK_TABLE")
        onCreate(db)
    }

    public fun getTaskList(){
        val db = readableDatabase
        val cursor = db.query(TASK_TABLE, null, null, null, null, null, null)
        cursor.moveToFirst()
        do {

        }while(cursor.moveToNext())
    }

}
const val DB_NAME = "plannerDb"
const val TASK_TABLE = "task"
const val DB_VERSION = 1