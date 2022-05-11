package com.example.emptyproject.fragments

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bolts.Task
import com.example.emptyproject.App
import com.example.emptyproject.models.Post
import com.example.emptyproject.PostsListAdapter
import com.example.emptyproject.R
import java.io.IOException

class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private var posts: ArrayList<Post> = arrayListOf()
    private var recyclerViewItemClickListener: OnPostsRecyclerViewItemClickListener? = null
    private var buttonStatistics: Button? = null
    private var buttonStatisticsClickListener: OnButtonStatisticsClickListener? = null

    companion object {
        fun newInstance(): PostsListFragment {
            return PostsListFragment()
        }
    }

    interface OnPostsRecyclerViewItemClickListener {
        fun onClick(id: Int)
    }

    interface OnButtonStatisticsClickListener {
        fun onClickStatistics()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        recyclerViewItemClickListener = try {
            activity as OnPostsRecyclerViewItemClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnPostsRecyclerViewItemClickListener")
        }

        buttonStatisticsClickListener = try {
            activity as OnButtonStatisticsClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnButtonStatisticsClickListener")

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postsRecyclerView: RecyclerView = view.findViewById(R.id.posts_recycler_view)
        buttonStatistics = view.findViewById(R.id.button_statistics)
        val buttonStatistics = buttonStatistics ?: return

        val db = App.instance?.dBHelper?.readableDatabase
        getPostsExecute(db).onSuccess({
            posts = it.result
            postsRecyclerView.layoutManager = LinearLayoutManager(activity)
            postsRecyclerView.adapter = PostsListAdapter(posts, recyclerViewItemClickListener!!)
            db?.close()
        }, Task.UI_THREAD_EXECUTOR).continueWith({
            if (it.isFaulted) {
                Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show()
            }
        }, Task.UI_THREAD_EXECUTOR)

        buttonStatistics.setOnClickListener {
            buttonStatisticsClickListener?.onClickStatistics()
        }
    }

    private fun getPostsExecute(db: SQLiteDatabase?): Task<ArrayList<Post>> {
        return Task.callInBackground {
            val cursor = db?.rawQuery(
                """SELECT title, email, body, post.id AS postId FROM post JOIN user ON userId == user.id""",
                null
            )
            val posts = arrayListOf<Post>()
            with(cursor) {
                while (this!!.moveToNext()) {
                    val post = Post()
                    post.id = getInt(getColumnIndexOrThrow("postId"))
                    post.title = getString(getColumnIndexOrThrow("title"))
                    post.email = getString(getColumnIndexOrThrow("email"))
                    post.body = getString(getColumnIndexOrThrow("body"))
                    posts.add(post)
                }
            }
            cursor?.close()
            posts
        }
    }
}