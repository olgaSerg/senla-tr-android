package com.example.emptyproject.fragments

import android.app.Activity
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyproject.App
import com.example.emptyproject.models.Post
import com.example.emptyproject.PostsListAdapter
import com.example.emptyproject.R

class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private var posts: ArrayList<Post> = arrayListOf()
    private var recyclerViewItemClickListener: OnPostsRecyclerViewItemClickListener? = null

    companion object {
        fun newInstance(): PostsListFragment {
            return PostsListFragment()
        }
    }

    interface OnPostsRecyclerViewItemClickListener {
        fun onClick(id: Int)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        recyclerViewItemClickListener = try {
            activity as OnPostsRecyclerViewItemClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnPostsRecyclerViewItemClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postsRecyclerView: RecyclerView = view.findViewById(R.id.posts_recycler_view)

        posts = getPosts()
        postsRecyclerView.layoutManager = LinearLayoutManager(activity)
        postsRecyclerView.adapter = PostsListAdapter(posts, recyclerViewItemClickListener!!)
    }

    private fun getPosts(): ArrayList<Post> {
        val db = App.instance?.dBHelper?.readableDatabase
        posts =  getPostsExecute(db)
        db?.close()
        return posts
    }

    private fun getPostsExecute(db: SQLiteDatabase?): ArrayList<Post> {
        val cursor = db?.rawQuery("""SELECT title, email, body, user.id FROM post JOIN user ON userId == user.id""", null)
        val posts = arrayListOf<Post>()
        with (cursor) {
            while (this!!.moveToNext()) {
                val post = Post()
                post.id = getInt(getColumnIndexOrThrow("id"))
                post.title = getString(getColumnIndexOrThrow("title"))
                post.email = getString(getColumnIndexOrThrow("email"))
                post.body = getString(getColumnIndexOrThrow("body"))
                posts.add(post)
            }
        }
        cursor?.close()
        return posts
    }

}