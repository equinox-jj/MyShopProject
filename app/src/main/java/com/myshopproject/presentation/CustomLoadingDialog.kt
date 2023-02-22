package com.myshopproject.presentation

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.myshopproject.databinding.CustomLoadingBinding

class CustomLoadingDialog(private val activity: Activity) {

    private lateinit var isDialog: AlertDialog

    fun showDialog() {
        val dialogBinding = CustomLoadingBinding.inflate(activity.layoutInflater)

        val dialog = AlertDialog.Builder(activity)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(false)
        isDialog = dialog.create()
        dialog.show()
    }

    fun hideDialog() {
        isDialog.dismiss()
    }
}