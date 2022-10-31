package app.king.mylibrary.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessScrollListener protected constructor() :
    RecyclerView.OnScrollListener() {

    private var mLoading = false // True if we are still waiting for the last set of data to load

    private var previousItemCount =
        0 // The total number of items in the dataSet after the last load

    var currentPage = 1
        private set // Always start at Page 1

    private var spanArray: IntArray? = null

    // Concrete classes should implement the Loading of more data entries
    abstract fun onLoadMore(current_page: Int)

    //abstract fun onScrollTop(isVisible: Boolean)

    // When you're RecyclerView supports refreshing, also refresh the count

    fun refresh() {
        currentPage = 1
        previousItemCount = 0
        mLoading = false
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager ?: LinearLayoutManager(recyclerView.context)

        val totalItemCount = layoutManager.itemCount
        val lastCompletelyVisibleItemPosition =
            when (layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.findLastVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    layoutManager.findLastVisibleItemPosition()
                }
                else -> {
                    (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                        spanArray)[0]
                }
            }

        if (mLoading) {

            // Check if current total is greater than previous
            if (totalItemCount > previousItemCount) {
                mLoading = false
                previousItemCount = totalItemCount
            }

        } else {

            if (totalItemCount >= 10) {
                // Check if the we've reached the end of the list
                if (lastCompletelyVisibleItemPosition == totalItemCount - 1 && totalItemCount != 0) {
                    onLoadMore(++currentPage)
                    mLoading = true
                }
            }
        }

    }
}
