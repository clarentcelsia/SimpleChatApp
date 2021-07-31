package com.example.chatminiproject.Util

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chatminiproject.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Util {

    fun setImageResource(view: View, imageUrl: String, target: ImageView) {
        if (imageUrl == "default") {
            target.setImageResource(R.drawable.defaultperson)
        } else
            Glide.with(view).load(imageUrl).into(target)
    }

    fun convertLongToTime(time: Long, pattern: String): String{
        val date = Date(time)
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    fun compareTime(time: ArrayList<Long>): Long {
        var start_time = 0L
        var end_time = 0L
        var current_time = 0L
        var i = 0

        if (time.size > 0 && time.size <= 1) {
            current_time = time[i]
        }
        do {
            if (time.size > 1) {
                start_time = time[i]
                end_time = time[i + 1]
                if (start_time > end_time) {
                    if (current_time != null) {
                        if (start_time > current_time)
                            current_time = start_time
                        else
                            current_time = current_time
                    } else
                        current_time = start_time
                } else {
                    if (current_time != null) {
                        if (end_time > current_time)
                            current_time = end_time
                        else
                            current_time = current_time
                    } else
                        current_time = end_time
                }

                i++
            }

        } while (i != (time.size - 1))

        return current_time
    }
}


