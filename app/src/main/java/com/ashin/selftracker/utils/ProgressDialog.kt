package com.ashin.selftracker.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.ashin.selftracker.R


class ProgressDialog(context: Context) : Dialog(context) {
    init {
        val wlmp = window?.attributes

        wlmp?.gravity = Gravity.CENTER_HORIZONTAL
        window?.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view = LayoutInflater.from(context).inflate(
            R.layout.custom_progress, null
        )
        setContentView(view)
    }
}