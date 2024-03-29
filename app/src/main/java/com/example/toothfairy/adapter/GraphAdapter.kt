package com.example.toothfairy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.toothfairy.R


class GraphAdapter(data: List<Float>?) : RecyclerView.Adapter<GraphAdapter.ViewHolder>() {
    private var data: List<Float>?
    var width = 0
    var height = 0
    var widthCount = 7              // 가로 항목의 개수 설정
    var heightCount = 24            // 세로 항목의 길이 설정
    var graphLineWidth = 15

    /** ViewHolder 객체를 생성 후 리턴 */
    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        
        // item_graph_line 레이아웃을 뷰로 설정 (View는 해당 레이아웃을 가지게 됨)
        // GraphAdapter를 통해 데이터를 item_graph_line 형식으로 생성할 수 있게 됨
        val v: View = inflater.inflate(R.layout.sub_item_graph_line, viewGroup, false)
        
        width = viewGroup.width
        height = viewGroup.height

        if (width <= 0) {
            val dm = viewGroup.context.resources.displayMetrics
            width = dm.widthPixels
        }
        
        val params = v.layoutParams
        params.width = width / widthCount
        params.height = ViewGroup.LayoutParams.MATCH_PARENT

        v.layoutParams = params

        return ViewHolder(v)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, i: Int) {
        val wearingTime: Int = data!![i].toInt()

        val currentHeight: Int = if (wearingTime > 0) { height * wearingTime / heightCount } else { 0 }

        val line1_param = holder.graphLine.layoutParams
        
        line1_param.width = graphLineWidth
        line1_param.height = currentHeight

        holder.graphLine.layoutParams = line1_param
    }

    override fun getItemCount(): Int {
        return if (data != null) data!!.size else 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val graphLine: View
        val graphDate: TextView

        init {
            graphLine = itemView.findViewById(R.id.graphLine)
            graphDate = itemView.findViewById(R.id.graphDate)
        }
    }

    init {
        this.data = data
    }
}