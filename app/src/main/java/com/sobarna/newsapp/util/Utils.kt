package com.sobarna.newsapp.util


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    fun formatDateTime(dateTimeString: String): String {
        val inputFormats = arrayOf(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        )

        val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.ENGLISH)

        for (inputFormatString in inputFormats) {
            val inputFormat = SimpleDateFormat(inputFormatString, Locale.ENGLISH)
            return try {
                val date = inputFormat.parse(dateTimeString) as Date
                outputFormat.format(date)
            } catch (e: ParseException) {
                return ""
            }
        }
        return ""
    }

}