package com.example.emptyproject.loaders

import android.content.Context
import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import com.example.emptyproject.App
import com.example.emptyproject.fragments.POST_ID
import com.example.emptyproject.models.CommentModel

class CommentsLoader(context: Context, args: Bundle) : AsyncTaskLoader<ArrayList<CommentModel>>(context) {

    private val arguments = args.getInt(POST_ID)

    override fun loadInBackground(): ArrayList<CommentModel> {
        val db = App.instance?.dBHelper?.readableDatabase ?: return arrayListOf()
        val selectionArgs = arguments.toString()
        val cursor =
            db.rawQuery(
                """
                        SELECT user.email, comment.text, comment.rate, comment.id
                        FROM comment
                            JOIN post ON post.id == comment.postId
                            JOIN user ON user.id == comment.userId
                        WHERE post.id == ?
                        ORDER BY comment.id
                        """, arrayOf(selectionArgs)
            )
        val comments = arrayListOf<CommentModel>()

        with (cursor) {
            while (moveToNext()) {
                val comment = CommentModel()
                comment.email = getString(getColumnIndexOrThrow("email"))
                comment.text = getString(getColumnIndexOrThrow("text"))
                comment.rate = getInt(getColumnIndexOrThrow("rate"))
                comment.commentId = getInt(getColumnIndexOrThrow("id"))
                comments.add(comment)
            }
        }
        cursor.close()
        return comments
    }

    override fun onStartLoading() {
        forceLoad()
    }
}