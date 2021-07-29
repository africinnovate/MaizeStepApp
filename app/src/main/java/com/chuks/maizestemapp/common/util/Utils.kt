package com.chuks.maizestemapp.common.util

import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object MyUtils{

    fun getDateInMilliSeconds(
        givenDateString: String?,
        format: String
    ): Long {
        val sdf = SimpleDateFormat(format, Locale.US)
        var timeInMilliseconds: Long = 1
        try {
            val mDate = sdf.parse(givenDateString)
            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeInMilliseconds
    }

     fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
        try {
            val netDate = Date(epoc*1000)
            return netDate.toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }


}