package com.example.emptyproject.loaders

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import com.example.emptyproject.App
import com.example.emptyproject.models.Post

class PostsListLoader(context: Context) : AsyncTaskLoader<ArrayList<Post>>(context) {
    override fun loadInBackground(): ArrayList<Post> {
        val db = App.instance?.dBHelper?.readableDatabase ?: return arrayListOf()
        val cursor = db.rawQuery(
            """SELECT title, email, body, post.id AS postId FROM post JOIN user ON userId == user.id""",
            null
        )
        val posts = arrayListOf<Post>()
        with(cursor) {
            while (moveToNext()) {
                val post = Post()
                post.id = getInt(getColumnIndexOrThrow("postId"))
                post.title = getString(getColumnIndexOrThrow("title"))
                post.email = getString(getColumnIndexOrThrow("email"))
                post.body = getString(getColumnIndexOrThrow("body"))
                posts.add(post)
            }
        }
        cursor.close()
        return posts
    }

    override fun onStartLoading() {
        forceLoad()
    }
}
