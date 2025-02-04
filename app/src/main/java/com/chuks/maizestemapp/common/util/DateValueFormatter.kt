package com.chuks.maizestemapp.common.util

import androidx.appcompat.widget.ViewUtils
import com.chuks.maizestemapp.common.data.Insect
import com.github.mikephil.charting.components.AxisBase

import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.Utils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateValueFormatter(var datesList: List<Insect>) :
    ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase): String {
/*
Depends on the position number on the X axis, we need to display the label,
 Here, this is the logic to convert the float value to integer so that I can get the value from array
 based on that integer and can convert it to the required value here, month and date as value.
  This is required for my data to show properly, you can customize according to your needs.
*/
        var position = Math.round(value)
        val sdf = SimpleDateFormat("MMM dd")
        if (value > 1 && value < 2) {
            position = 0
        } else if (value > 2 && value < 3) {
            position = 1
        } else if (value > 3 && value < 4) {
            position = 2
        } else if (value > 4 && value <= 5) {
            position = 3
        }
        return if (position < datesList.size) sdf.format(
            Date(
//                MyUtils.getDateInMilliSeconds(
//                    datesList[position].timeStamp.toString(),
//                    "yyyy-MM-dd"
//                )
                        MyUtils.getDateTimeFromEpocLongOfSeconds(datesList[position].timeStamp.toLong())

                )
        ) else ""
    }
}


