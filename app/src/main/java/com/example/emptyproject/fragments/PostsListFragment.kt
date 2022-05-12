package com.example.emptyproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyproject.models.Post
import com.example.emptyproject.adapters.PostsListAdapter
import com.example.emptyproject.R
import com.example.emptyproject.loaders.PostsListLoader

class PostsListFragment : Fragment(R.layout.fragment_posts_list), LoaderManager.LoaderCallbacks<ArrayList<Post>> {

    private var recyclerViewItemClickListener: OnPostsRecyclerViewItemClickListener? = null
    private var buttonStatistics: Button? = null
    private var buttonStatisticsClickListener: OnButtonStatisticsClickListener? = null
    private var postsRecyclerView: RecyclerView? = null
    private var mLoaderManager: LoaderManager? = null

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

        postsRecyclerView = view.findViewById(R.id.posts_recycler_view)
        buttonStatistics = view.findViewById(R.id.button_statistics)

        val buttonStatistics = buttonStatistics ?: return

        mLoaderManager = LoaderManager.getInstance(this)
        if (mLoaderManager != null) {
            mLoaderManager?.initLoader(1, null, this)
        }

        buttonStatistics.setOnClickListener {
            buttonStatisticsClickListener?.onClickStatistics()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Post>> {
        return PostsListLoader(requireActivity())
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Post>>, data: ArrayList<Post>?) {
        val postsRecyclerView = postsRecyclerView ?: return
        postsRecyclerView.layoutManager = LinearLayoutManager(activity)
        if (data != null) {
            postsRecyclerView.adapter =
                recyclerViewItemClickListener?.let { PostsListAdapter(data, it) }
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Post>>) {
    }
}