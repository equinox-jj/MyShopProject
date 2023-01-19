package com.myshopproject.utils

import android.view.View
import java.text.NumberFormat
import java.util.*

fun View.setVisibilityVisible() {
    this.visibility = View.VISIBLE
}

fun View.setVisibilityGone() {
    this.visibility = View.GONE
}

fun String.toIDRPrice(): String {
    val localeID = Locale("in", "ID")
    val doubleValue = this.toDoubleOrNull() ?: return this
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    numberFormat.minimumFractionDigits = 0
    return numberFormat.format(doubleValue)
}