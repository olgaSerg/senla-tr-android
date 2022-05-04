package com.example.emptyproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import androidx.recyclerview.widget.RecyclerView
import com.example.emptyproject.fragments.PostsListFragment
import com.example.emptyproject.models.Post


class PostsListAdapter(private val posts: List<Post>,
                       private val recyclerViewItemClickListener: PostsListFragment.OnPostsRecyclerViewItemClickListener):
    RecyclerView.Adapter<PostsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        holder.title.text = posts[adapterPosition].title
        holder.email.text = posts[adapterPosition].email
        holder.body.text = posts[adapterPosition].body
        holder.itemView.setOnClickListener {
            val clickedPostId = posts[adapterPosition].id
            recyclerViewItemClickListener.onClick(clickedPostId!!)
            Log.d("msg", clickedPostId.toString())
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val email: TextView = itemView.findViewById(R.id.email)
        val body: TextView = itemView.findViewById(R.id.body)
    }
}
