package com.example.emptyproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyproject.R
import com.example.emptyproject.models.Statistics

class StatisticsListAdapter(private val statistics: List<Statistics>):
    RecyclerView.Adapter<StatisticsListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.statistics_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       bind(holder, holder.adapterPosition)
    }

    private fun bind(holder: ViewHolder, adapterPosition: Int) {
        holder.postName.text = statistics[adapterPosition].postName
        holder.commentsCount.text = statistics[adapterPosition].commentsCount.toString()
        holder.averageRate.text = statistics[adapterPosition].averageRate.toString()
    }

    override fun getItemCount(): Int {
        return statistics.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postName: TextView = itemView.findViewById(R.id.text_view_post)
        val commentsCount: TextView = itemView.findViewById(R.id.text_view_comments_count)
        val averageRate: TextView = itemView.findViewById(R.id.text_view_average_rate)
    }
}
