package com.njves.schoolnetwork.Models

import com.njves.schoolnetwork.App
import java.io.*

class LogFile{
    fun v(text : String){
        val dir = App.getAppContext().cacheDir
        val file = File(dir, "log.txt")
        val out = BufferedWriter(FileWriter(file))
        out.write(getFileText()+"\n"+text)
        out.flush()
    }
    companion object{
        fun getFileText() : String?{
            val dir = App.getAppContext().cacheDir
            val file = File(dir, "log.txt")
            val input = BufferedReader(InputStreamReader(FileInputStream(file)))
            return input.readLine()
        }
    }
}