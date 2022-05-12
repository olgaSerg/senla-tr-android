package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyproject.R
import com.example.emptyproject.adapters.StatisticsListAdapter
import com.example.emptyproject.loaders.StatisticsLoader
import com.example.emptyproject.models.Statistics

class StatisticsFragment : Fragment(R.layout.fragment_statistics), LoaderManager.LoaderCallbacks<ArrayList<Statistics>> {

    private var statisticsRecyclerView: RecyclerView? = null
    private var mLoaderManager: LoaderManager? = null

    companion object {
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statisticsRecyclerView = view.findViewById(R.id.statistics_recycler_view)
        mLoaderManager = LoaderManager.getInstance(this)
        if (mLoaderManager != null) {
            mLoaderManager!!.initLoader(1, arguments, this)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Statistics>> {
       return StatisticsLoader(requireActivity())
    }

    override fun onLoadFinished(
        loader: Loader<ArrayList<Statistics>>,
        data: ArrayList<Statistics>?
    ) {
        statisticsRecyclerView?.layoutManager = LinearLayoutManager(activity)
        if (data != null) {
            statisticsRecyclerView?.adapter = StatisticsListAdapter(data)
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Statistics>>) {
        loader.cancelLoad()
    }
}