package com.example.emptyproject

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.ArrayList


internal class FilesListAdapter(context: Context, filesList: ArrayList<FileObject>) :
    ArrayAdapter<FileObject>(context, R.layout.simple_list_item_2, filesList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val fileObject = getItem(position) as FileObject
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.simple_list_item_2, null)
        }
        convertView!!.findViewById<TextView>(R.id.text1).text = fileObject.name

        val lastDate = fileObject.date
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val dateText: String = dateFormat.format(lastDate)

        convertView.findViewById<TextView>(R.id.text2).text = dateText
        return convertView
    }
}