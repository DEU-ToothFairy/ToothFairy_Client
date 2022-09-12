package com.example.toothfairy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.toothfairy.R
import com.example.toothfairy.data.CalendarDate
import com.example.toothfairy.util.MyApplication
import kotlin.collections.ArrayList

class CalendarAdapter(private val dataSet: ArrayList<CalendarDate>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>(){

    /** RecyclerView의 클릭 이벤트 처리를 위한 리스너 인터페이스 */
    interface OnItemClickListener{
        fun onClick(view:View, position:Int)
    }

    lateinit var onItemClickListener: OnItemClickListener

    var drawable: Drawable? = null

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val dayTextView: TextView = view.findViewById(R.id.dayTextView)
        val calendarCell: LinearLayout = view.findViewById(R.id.calendarCell)
    }

    // ViewHolder를 생성하는 메소드
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.sub_calendar_cell, viewGroup, false)

        drawable = ContextCompat.getDrawable(view.context, R.drawable.calendar_select)

        return ViewHolder(view)
    }

    // ViewHolder에 데이터를 바인딩해주는 메소드
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTextView.text = dataSet[position].date.dayOfMonth.toString()
        holder.dayTextView.text = dataSet[position].day

        if(onItemClickListener != null){
            holder.calendarCell.setOnClickListener(View.OnClickListener(){
                onItemClickListener.onClick(it, position) // 람다 형태로 OnClickListener 인터페이스를 구현
            })
        }
    }

    override fun getItemCount(): Int = dataSet.size
}