package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.example.emptyproject.App
import com.example.emptyproject.models.CommentModel
import com.example.emptyproject.R

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
        val textViewEmail = textViewEmail ?: return
        val textViewBodyComment = textViewBodyComment ?: return

        val postId = arguments?.getInt("id")
        val comment = getComments(postId!!)
//        textViewEmail.text = comment.userEmail
//        textViewBodyComment.text = comment.text
    }

    private fun getComments(postId: Int): CommentModel {
        val db = App.instance?.dBHelper?.readableDatabase
        val selectionArgs = postId.toString()
        val cursor =
            db!!.rawQuery(
                """SELECT email, text FROM comment JOIN user ON userId == user.id WHERE user.id == ?""",
                arrayOf(selectionArgs)
            )
        val comment = CommentModel()
        with(cursor) {
            moveToNext()
            comment.userEmail = getString(getColumnIndexOrThrow("email"))
            comment.text = getString(getColumnIndexOrThrow("text"))
        }
        displayComments(comment)
        cursor?.close()
        db?.close()
        return comment
    }

    private fun displayComments(comment: CommentModel) {
        val textViewEmail = textViewEmail ?: return
        val textViewBodyComment = textViewBodyComment ?: return
        textViewEmail.text = comment.userEmail
        textViewBodyComment.text = comment.text
    }
}