package app.king.mylibrary.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener(
    var currentPage: Int = 1,
    private val visibleThreshold: Int = 4,
) :
    RecyclerView.OnScrollListener() {

    private var previousTotalItemCount = 0
    private var loading = true
    private var startingPageIndex = 1

    init {
        startingPageIndex = currentPage
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val mLayoutManager = view.layoutManager ?: return
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    mLayoutManager.findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItemPosition =
                    mLayoutManager.findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItemPosition =
                    mLayoutManager.findLastVisibleItemPosition()
            }
        }
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    fun reset() {
        currentPage = 0
        previousTotalItemCount = 0
        loading = true
    }

    abstract fun onLoadMore(page: Int)

}