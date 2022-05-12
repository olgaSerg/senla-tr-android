package com.example.emptyproject.loaders

import android.content.Context
import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import com.example.emptyproject.App
import com.example.emptyproject.fragments.POST_ID
import com.example.emptyproject.models.PostDetails

class PostDetailLoader(context: Context, args: Bundle): AsyncTaskLoader<PostDetails>(context) {

    private val arguments = args.getInt(POST_ID)
    override fun loadInBackground(): PostDetails {
        val db = App.instance?.dBHelper?.readableDatabase ?: return PostDetails()

        val selectionArgs = arguments.toString()
        val cursor =
            db.rawQuery(
                """SELECT title, email, body, post.id AS postId, fullName FROM post JOIN user ON userId == user.id WHERE post.id == ?""",
                arrayOf(
                    selectionArgs
                )
            )
        val postDetails = PostDetails()
        with(cursor) {
            moveToNext()
            postDetails.id = getInt(getColumnIndexOrThrow("postId"))
            postDetails.title = getString(getColumnIndexOrThrow("title"))
            postDetails.email = getString(getColumnIndexOrThrow("email"))
            postDetails.fullName = getString(getColumnIndexOrThrow("fullName"))
            postDetails.body = getString(getColumnIndexOrThrow("body"))
        }
        cursor.close()
        return postDetails
    }

    override fun onStartLoading() {
        forceLoad()
    }
}