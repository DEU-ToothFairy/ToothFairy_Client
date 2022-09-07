package com.example.toothfairy.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.toothfairy.R
import com.example.toothfairy.data.CalendarDate
import kotlin.collections.ArrayList

class CalendarAdapter(private val dataSet: ArrayList<CalendarDate>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>(){

    var drawable: Drawable? = null
    private lateinit var itemClickListener: AdapterView.OnItemClickListener

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val dateTextView: TextView = view.findViewById(R.id.date_cell)
        val dayTextView: TextView = view.findViewById(R.id.day_cell)
    }

    // ViewHolder를 생성하는 메소드
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.sub_calendar_cell, viewGroup, false)

        drawable = ContextCompat.getDrawable(view.context, R.drawable.calendar_select)

        return ViewHolder(view)
    }

    // ViewHolder에 데이터를 바인딩해주는 메소드
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTextView.text = dataSet[position].date
        holder.dayTextView.text = dataSet[position].day
    }

    override fun getItemCount(): Int = dataSet.size
}