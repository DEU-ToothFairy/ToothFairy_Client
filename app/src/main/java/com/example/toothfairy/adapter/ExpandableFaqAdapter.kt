package com.example.toothfairy.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toothfairy.R
import com.example.toothfairy.data.Faq
import com.example.toothfairy.util.ToggleAnimation

class ExpandableFaqAdapter(private val faqList: List<Faq>) : RecyclerView.Adapter<ExpandableFaqAdapter.ViewHolder>(){
    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        /**
         * 데이터를 뷰에 연결하는 메소드
         */
        @SuppressLint("SetTextI18n")
        fun bind(position: Int, faq:Faq){
            val questionIdx = itemView.findViewById<TextView>(R.id.questionIdx)
            val question = itemView.findViewById<TextView>(R.id.questionTv)
            val answer = itemView.findViewById<TextView>(R.id.answerTv)
            val checkDown = itemView.findViewById<ImageButton>(R.id.checkDown)
            val expandLayout = itemView.findViewById<LinearLayout>(R.id.expandLayout)

            questionIdx.text = "Q${position+1}."
            question.text = faq.question
            answer.text = faq.answer

            // faq 클릭 시 토클
            checkDown.setOnClickListener{
                val show = toggleLayout(!faq.isExpanded, it, expandLayout)
                faq.isExpanded = show
            }
        }

        /**
         * ExpandLayout 토글 메소드
         */
        private fun toggleLayout(isExpanded: Boolean, view: View, expandLayout:LinearLayout): Boolean{
            ToggleAnimation.toggleArrow(view, isExpanded)

            if(isExpanded) ToggleAnimation.expand(expandLayout)
            else ToggleAnimation.collapse(expandLayout)

            return isExpanded
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sub_faq_item_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, faqList[position])
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}