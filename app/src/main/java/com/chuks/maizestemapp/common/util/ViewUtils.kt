package com.chuks.maizestemapp.common.util

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.widget.Toast
import android.text.Spanned
import android.text.style.BulletSpan
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Use Kotlin [ExtensionFunctionType] to use [showToast] as a context extension function.
 * @param message to show on the toast
 ***/
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

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