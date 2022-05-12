package com.example.emptyproject.adapters

import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bolts.Task
import com.example.emptyproject.App
import com.example.emptyproject.R
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
    }

    private fun bind(holder: ViewHolder, adapterPosition: Int) {
        holder.email.text  = comments[adapterPosition].email
        holder.comment.text = comments[adapterPosition].text
        holder.rate.text = comments[adapterPosition].rate.toString()

        val db = App.instance?.dBHelper?.writableDatabase ?: return
        setUpdateRateClickListeners(holder, db)
    }

    private fun setUpdateRateClickListeners(holder: ViewHolder, db: SQLiteDatabase) {
        val commentId = comments[holder.adapterPosition].commentId
        var commentRate = comments[holder.adapterPosition].rate
        if (commentRate != null) {
            holder.imageButtonThumbUp.setOnClickListener {
                commentRate++
                updateRate(commentId!!, holder, commentRate, db)
            }
            holder.imageButtonThumbDown.setOnClickListener {
                commentRate--
                updateRate(commentId!!, holder, commentRate, db)
            }
        }
    }

    private fun updateRate(
        commentId: Int,
        holder: ViewHolder,
        commentRate: Int,
        db: SQLiteDatabase
    ) {
        setNewRate(commentId, commentRate, db).onSuccess({
            holder.rate.text = it.result.toString()
        }, Task.UI_THREAD_EXECUTOR)
    }


    private fun setNewRate(commentId: Int, commentRate: Int, db: SQLiteDatabase): Task<Int> {
        return Task.callInBackground {
            db.execSQL("""UPDATE comment SET rate = $commentRate WHERE comment.id == $commentId""")
        }.onSuccessTask { getRate(db, commentId) }
    }

    private fun getRate(db: SQLiteDatabase, commentId: Int): Task<Int> {
        return Task.callInBackground {
            val cursor =
                db.rawQuery("""SELECT rate FROM comment WHERE id == $commentId""", null)
            with(cursor) {
                moveToNext()
                val rate = getInt(getColumnIndexOrThrow("rate"))
                rate
            }
        }
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
