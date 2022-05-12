package com.example.emptyproject.loaders

import android.content.Context
import androidx.core.database.getFloatOrNull
import androidx.loader.content.AsyncTaskLoader
import com.example.emptyproject.App
import com.example.emptyproject.models.Statistics

class StatisticsLoader(context: Context) : AsyncTaskLoader<ArrayList<Statistics>>(context)  {

    private var statistics: ArrayList<Statistics> = arrayListOf()

    override fun loadInBackground(): ArrayList<Statistics> {
        val db = App.instance?.dBHelper?.readableDatabase ?: return arrayListOf()
        val cursor =
            db.rawQuery(
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
                statistic.postName = getString(getColumnIndexOrThrow("title"))!!
                statistic.commentsCount = getInt(getColumnIndexOrThrow("commentsCount"))

                val avgRateColIndex = getColumnIndexOrThrow("avgRate")
                statistic.averageRate = getFloatOrNull(avgRateColIndex) ?: 0.0f
                statistics.add(statistic)
            }
        }
        return statistics
    }

    override fun onStartLoading() {
        forceLoad()
    }
}