package com.example.emptyproject.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import bolts.Task
import com.example.emptyproject.App
import com.example.emptyproject.models.PostDetails
import com.example.emptyproject.R
import com.example.emptyproject.providers.PostDetailsProvider

const val POST_ID = "id"

class PostDetailsListFragment : Fragment(R.layout.fragment_post_detail) {

    private var title: TextView? = null
    private var email: TextView? = null
    private var fullName: TextView? = null
    private var body: TextView? = null
    private var button: Button? = null
    private var clickButtonComments: OnClickButtonComments? = null

    companion object {
        fun newInstance(id: Int): PostDetailsListFragment {
            val args = Bundle()
            args.putInt(POST_ID, id)
            val postDetailListFragment = PostDetailsListFragment()
            postDetailListFragment.arguments = args
            return postDetailListFragment
        }
    }

    interface OnClickButtonComments {
        fun onClickButtonComment(postId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        clickButtonComments = try {
            activity as OnClickButtonComments
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnClickButtonComments")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.title)
        email= view.findViewById(R.id.email)
        fullName= view.findViewById(R.id.full_name)
        body= view.findViewById(R.id.body)
        button = view.findViewById(R.id.button_comment)

        val button = button ?: return

        val clickedPostId = arguments?.getInt("id") ?: return
        val db = App.instance?.dBHelper?.readableDatabase ?: return
        val postDetailsProvider = PostDetailsProvider()

        postDetailsProvider.getPostDetails(clickedPostId, db).onSuccess({
            displayPostDetails(it.result)
            val postDetails = it.result
            button.setOnClickListener {
                clickButtonComments?.onClickButtonComment(postDetails.id!!)
            }
            db.close()
        }, Task.UI_THREAD_EXECUTOR)
    }

    private fun displayPostDetails(postDetails: PostDetails) {
        val title = title ?: return
        val email = email ?: return
        val fullName = fullName ?: return
        val body = body ?: return

        title.text = postDetails.title
        email.text = postDetails.email
        fullName.text = postDetails.fullName
        body.text = postDetails.body
    }
}