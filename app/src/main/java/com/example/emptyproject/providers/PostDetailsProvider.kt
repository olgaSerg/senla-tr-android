package com.example.emptyproject.providers

import android.database.sqlite.SQLiteDatabase
import bolts.Task
import com.example.emptyproject.models.PostDetails

class PostDetailsProvider {

    fun getPostDetails(id: Int, db: SQLiteDatabase): Task<PostDetails> {
        return Task.callInBackground {
            val selectionArgs = id.toString()
            val cursor =
                db.rawQuery(
                    """SELECT title, email, body, user.id, firstName || ' ' || lastName AS fullName FROM post JOIN user ON userId == user.id WHERE user.id == ?""",
                    arrayOf(
                        selectionArgs
                    )
                )
            val postDetails = PostDetails()
            with(cursor) {
                moveToNext()
                postDetails.id = getInt(getColumnIndexOrThrow("id"))
                postDetails.title = getString(getColumnIndexOrThrow("title"))
                postDetails.email = getString(getColumnIndexOrThrow("email"))
                postDetails.fullName = getString(getColumnIndexOrThrow("fullName"))
                postDetails.body = getString(getColumnIndexOrThrow("body"))
            }
            cursor.close()
            postDetails
        }
    }
}