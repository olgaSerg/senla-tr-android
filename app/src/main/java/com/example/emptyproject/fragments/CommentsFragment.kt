package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bolts.Task
import com.example.emptyproject.App
import com.example.emptyproject.CommentsListAdapter
import com.example.emptyproject.models.CommentModel
import com.example.emptyproject.R

class CommentsFragment : Fragment(R.layout.fragment_comments) {

    private var comments: ArrayList<CommentModel> = arrayListOf()

    companion object {
        fun newInstance(postId: Int): CommentsFragment {
            val args = Bundle()
            args.putInt(POST_ID, postId)
            val commentsFragment = CommentsFragment()
            commentsFragment.arguments = args
            return commentsFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = arguments?.getInt("id")
        val commentsRecyclerView: RecyclerView = view.findViewById(R.id.comments_recycler_view)

        getComments(postId!!).onSuccess({
            comments = it.result
            commentsRecyclerView.layoutManager = LinearLayoutManager(activity)
            commentsRecyclerView.adapter = CommentsListAdapter(comments)
        }, Task.UI_THREAD_EXECUTOR)
    }

    private fun getComments(postId: Int): Task<ArrayList<CommentModel>> {
        return Task.callInBackground {
            val db = App.instance?.dBHelper?.readableDatabase
            val selectionArgs = postId.toString()
            val cursor =
                db!!.rawQuery(
                    """
                        SELECT user.email, comment.text, comment.rate, comment.id
                        FROM comment
                            JOIN post ON post.id == comment.postId
                            JOIN user ON user.id == comment.userId
                        WHERE post.id == ?
                        ORDER BY comment.id
                        """, arrayOf(selectionArgs)
                )
            val comments = arrayListOf<CommentModel>()

            with(cursor) {
                while (moveToNext()) {
                    val comment = CommentModel()
                    comment.email = this?.getString(this.getColumnIndexOrThrow("email"))
                    comment.text = this?.getString(getColumnIndexOrThrow("text"))
                    comment.rate = this?.getInt(getColumnIndexOrThrow("rate"))
                    comment.commentId = this?.getInt(getColumnIndexOrThrow("id"))
                    comments.add(comment)
                }
            }
            cursor.close()
            comments
        }
    }
}
//        }.onSuccess {
//            displayComments(it.result)
//        }.continueWith(finish@{
//            if (it.isFaulted) {
//                when (it.error) {
//                    is IOException -> {
//                        textViewEmail?.text = getString(R.string.error)
//                    }
//                    is CancellationException -> {
//                        return@finish
//                    }
//                }
//            }
//        }, Task.UI_THREAD_EXECUTOR)
