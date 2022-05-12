package com.example.emptyproject.providers

import android.database.sqlite.SQLiteDatabase
import bolts.Task
import com.example.emptyproject.models.Post

class PostsProvider {

    fun getPostsExecute(db: SQLiteDatabase): Task<ArrayList<Post>> {
        return Task.callInBackground {
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
            posts
        }
    }
}