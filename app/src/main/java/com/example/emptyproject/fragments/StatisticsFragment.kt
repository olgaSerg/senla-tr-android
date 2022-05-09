package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.database.getFloatOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bolts.Task
import com.example.emptyproject.App
import com.example.emptyproject.R
import com.example.emptyproject.StatisticsListAdapter
import com.example.emptyproject.models.Statistics

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private var statistics: ArrayList<Statistics> = arrayListOf()
    private var statisticsRecyclerView: RecyclerView? = null

    companion object {
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statisticsRecyclerView = view.findViewById(R.id.statistics_recycler_view)

        val statisticsRecyclerView = statisticsRecyclerView ?: return

        getStatistics().onSuccess({
            statistics = it.result
            statisticsRecyclerView.layoutManager = LinearLayoutManager(activity)
            statisticsRecyclerView.adapter = StatisticsListAdapter(statistics)
        }, Task.UI_THREAD_EXECUTOR)
    }

    private fun getStatistics(): Task<ArrayList<Statistics>> {
        return Task.callInBackground {
            val db = App.instance?.dBHelper?.readableDatabase
            val cursor =
                db!!.rawQuery(
                    """
                        SELECT post.id, post.title, COUNT(comment.id) AS commentsCount, AVG(comment.rate) AS avgRate FROM post 
                            LEFT JOIN comment ON post.id == comment.postId
                        GROUP BY postId
                        ORDER BY commentsCount DESC
                        """, null
                )
            statistics.clear()

            with(cursor) {
                while (moveToNext()) {
                    val statistic = Statistics()
                    statistic.postName = this?.getString(this.getColumnIndexOrThrow("title"))!!
                    statistic.commentsCount = getInt(getColumnIndexOrThrow("commentsCount"))

                    val avgRateColIndex = getColumnIndexOrThrow("avgRate")
                    statistic.averageRate = getFloatOrNull(avgRateColIndex) ?: 0.0f
                    statistics.add(statistic)
                }
                statistics
            }
        }
    }
}