package com.example.emptyproject.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bolts.Task
import com.example.emptyproject.App
import com.example.emptyproject.models.Post
import com.example.emptyproject.PostsListAdapter
import com.example.emptyproject.R
import com.example.emptyproject.providers.PostsProvider

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        recyclerViewItemClickListener = try {
            activity as OnPostsRecyclerViewItemClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnPostsRecyclerViewItemClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postsRecyclerView: RecyclerView = view.findViewById(R.id.posts_recycler_view)

        val db = App.instance?.dBHelper?.readableDatabase ?: return
        val postsProvider = PostsProvider()
        postsProvider.getPostsExecute(db).onSuccess({
            posts = it.result
            postsRecyclerView.layoutManager = LinearLayoutManager(activity)
            postsRecyclerView.adapter = PostsListAdapter(posts, recyclerViewItemClickListener!!)
            db.close()
        }, Task.UI_THREAD_EXECUTOR)
    }
}