package com.myshopproject.presentation.profile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.myshopproject.R

@SuppressLint("ViewHolder", "CutPasteId", "InflateParams")
class CustomSpinnerAdapter(
    context: Context,
    private var images: IntArray,
    private var language: Array<String>
) : BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.custom_spinner, null)
        val icon = view.findViewById<View>(R.id.iconLanguage) as ImageView?
        val languageText = view.findViewById<View>(R.id.tvLanguage) as TextView?

        icon!!.setImageResource(images[position])
        languageText!!.text = language[position]
        view.setPadding(0, view.paddingTop, 0, view.paddingBottom)
        return view
    }
}