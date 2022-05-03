package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), PostsListFragment.OnPostsRecyclerViewItemClickListener,
    PostDetailsListFragment.OnClickButtonComments {
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, PostsListFragment.newInstance())
            commit()
        }
        dbHelper.writableDatabase
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun onClick(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, PostDetailsListFragment.newInstance(id))
            addToBackStack("PostDetails")
            commit()
        }
    }

    override fun onClickButtonComment(postId: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, CommentsFragment.newInstance(postId))
//            addToBackStack("PostDetails")
            commit()
        }
    }
}