package com.example.emptyproject.providers

import android.database.sqlite.SQLiteDatabase
import bolts.Task
import com.example.emptyproject.R
import com.example.emptyproject.models.CommentModel
import java.io.IOException
import java.util.concurrent.CancellationException

class CommentsProvider {

    fun getComments(postId: Int, db: SQLiteDatabase): Task<CommentModel> {
        return Task.callInBackground {
            val selectionArgs = postId.toString()
            val cursor =
                db.rawQuery(
                    """SELECT email, text FROM comment JOIN user ON userId == user.id WHERE user.id == ?""",
                    arrayOf(selectionArgs)
                )
            val comment = CommentModel()

            with(cursor) {
                moveToNext()
                comment.userEmail = getString(getColumnIndexOrThrow("email"))
                comment.text = getString(getColumnIndexOrThrow("text"))
                cursor?.close()
                db.close()
                comment
            }
        }
    }
}