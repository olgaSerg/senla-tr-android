package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import bolts.Task
import com.example.emptyproject.App
import com.example.emptyproject.models.CommentModel
import com.example.emptyproject.R
import com.example.emptyproject.providers.CommentsProvider
import java.io.IOException
import java.util.concurrent.CancellationException

class CommentsFragment : Fragment(R.layout.fragment_comments) {

    private var textViewEmail: TextView? = null
    private var textViewBodyComment: TextView? = null

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

        textViewEmail = view.findViewById(R.id.text_view_email_comments)
        textViewBodyComment = view.findViewById(R.id.text_view_body_comments)

        val postId = arguments?.getInt("id")
        val db = App.instance?.dBHelper?.readableDatabase ?: return
        val commentsProvider = CommentsProvider()
        commentsProvider.getComments(postId!!, db).onSuccess {
            displayComments(it.result)
        }.continueWith(finish@{
            if (it.isFaulted) {
                when (it.error) {
                    is IOException -> {
                        textViewEmail?.text = getString(R.string.error)
                    }
                    is CancellationException -> {
                        return@finish
                    }
                }
            }
        }, Task.UI_THREAD_EXECUTOR)
    }

    private fun displayComments(comment: CommentModel) {
        val textViewEmail = textViewEmail ?: return
        val textViewBodyComment = textViewBodyComment ?: return
        textViewEmail.text = comment.userEmail
        textViewBodyComment.text = comment.text
    }
}