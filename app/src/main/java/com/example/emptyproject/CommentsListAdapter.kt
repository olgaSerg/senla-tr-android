package com.example.emptyproject

import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bolts.Task
import com.example.emptyproject.models.CommentModel


class CommentsListAdapter(private val comments: List<CommentModel>) :
    RecyclerView.Adapter<CommentsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        bind(holder, adapterPosition)
        val db = App.instance?.dBHelper?.writableDatabase
        val commentId = comments[adapterPosition].commentId

        var commentRate = comments[adapterPosition].rate
        var isIncrease: Boolean
        if (commentRate != null) {
            holder.imageButtonThumbUp.setOnClickListener {
                commentRate++
                isIncrease = true
                updateRate(db!!, commentId!!, holder, isIncrease)
            }
            holder.imageButtonThumbDown.setOnClickListener {
                commentRate--
                isIncrease = false
                updateRate(db!!, commentId!!, holder, isIncrease)
            }
        }
    }

    private fun bind(holder: ViewHolder, adapterPosition: Int) {
        holder.email.text  = comments[adapterPosition].email
        holder.comment.text = comments[adapterPosition].text
        holder.rate.text = comments[adapterPosition].rate.toString()
    }

    private fun getRate(db: SQLiteDatabase, commentId: Int, isIncrease: Boolean): Task<Int> {
        return Task.callInBackground {
            val sign = if (isIncrease) {
                "+"
            } else {
                "-"
            }
            db.execSQL("""UPDATE comment SET rate = rate $sign 1 WHERE comment.id == $commentId""")
            val cursor =
                db.rawQuery("""SELECT rate FROM comment WHERE id == $commentId""", null)
            with(cursor) {
                this?.moveToNext()
                val rate = this?.getInt(getColumnIndexOrThrow("rate"))
                rate
            }
        }
    }

    private fun updateRate(
        db: SQLiteDatabase,
        commentId: Int,
        holder: ViewHolder,
        isIncrease: Boolean
    ) {
        getRate(db, commentId, isIncrease).onSuccess({
            holder.rate.text = it.result.toString()
        }, Task.UI_THREAD_EXECUTOR)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val email: TextView = itemView.findViewById(R.id.email_item)
        val comment: TextView = itemView.findViewById(R.id.comment_item)
        val rate: TextView = itemView.findViewById(R.id.rate_item)
        val imageButtonThumbDown: ImageButton = itemView.findViewById(R.id.image_button_thumb_down)
        val imageButtonThumbUp: ImageButton = itemView.findViewById(R.id.image_button_thumb_up)
    }
}
