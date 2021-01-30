package com.example.myweatherapp.helper


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.myweatherapp.R

class CustomProgressDialog(
    context: Context?
) : Dialog(context!!) {

    fun show(message: String?) {
        try {
            if (!isShowing) {
                super.show()
            }
        } catch (e: Exception) {
            super.dismiss()
        }
    }

    override fun hide() {
        try {
            if (isShowing) {
                super.dismiss()
            }
        } catch (e: Exception) {
            super.dismiss()
        }
    }


    init {
        setContentView(R.layout.layout_loader)
        if (null != window) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}