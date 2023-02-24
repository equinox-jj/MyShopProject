package com.myshopproject.presentation

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.myshopproject.R

class CustomLoadingDialog(private val activity: Activity) {

    private lateinit var isDialog: AlertDialog

    fun showDialog() {
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_loading, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialog = builder.create()
//        isDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isDialog.show()
    }

    fun hideDialog() {
        isDialog.dismiss()
    }
}