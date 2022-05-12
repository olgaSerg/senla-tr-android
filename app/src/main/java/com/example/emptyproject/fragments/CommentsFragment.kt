package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyproject.adapters.CommentsListAdapter
import com.example.emptyproject.models.CommentModel
import com.example.emptyproject.R
import com.example.emptyproject.dialogs.CommentsDialogFragment
import com.example.emptyproject.loaders.CommentsLoader

class CommentsFragment : Fragment(R.layout.fragment_comments),
    LoaderManager.LoaderCallbacks<ArrayList<CommentModel>> {

    private var commentsRecyclerView: RecyclerView? = null
    private var mLoaderManager: LoaderManager? = null

    companion object {
        fun newInstance(postId: Int): CommentsFragment {
            val args = Bundle()
            args.putInt(POST_ID, postId)
            val commentsFragment = CommentsFragment()
            commentsFragment.arguments = args
            return commentsFragment
        }

        const val MESSAGE = "No comments to show"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view)
        mLoaderManager = LoaderManager.getInstance(this)
        if (mLoaderManager != null) {
            mLoaderManager!!.initLoader(1, arguments, this)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<CommentModel>> {
        return CommentsLoader(requireActivity(), args!!)
    }

    override fun onLoadFinished(
        loader: Loader<ArrayList<CommentModel>>,
        data: ArrayList<CommentModel>?
    ) {
        if (data != null) {
            if (data.size == 0) {
                val errorDialogFragment = CommentsDialogFragment()
                errorDialogFragment.show(childFragmentManager, "error")
            } else {
                if (commentsRecyclerView != null) {
                    commentsRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                    commentsRecyclerView!!.adapter = CommentsListAdapter(data)
                }
            }
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<CommentModel>>) {
        loader.cancelLoad()
    }
}
