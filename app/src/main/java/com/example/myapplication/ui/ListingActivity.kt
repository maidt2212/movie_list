package com.example.myapplication.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Movie
import com.example.myapplication.model.Result
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.buildwithnd.demotmdb.ui.listing.ListingViewModel
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Shows list of movie/show
 */
@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {

    private val list = ArrayList<Movie>()
    private val viewModel by viewModels<ListingViewModel>()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        subscribeUi()
    }

    private fun init() {
        title = "Search Movies"

        initSearchView()

        val layoutManager = GridLayoutManager(this, 2)
        rvMovies.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            rvMovies.context,
            layoutManager.orientation
        )

        rvMovies.addItemDecoration(dividerItemDecoration)
        moviesAdapter = MoviesAdapter(this, list)
        rvMovies.adapter = moviesAdapter
        initScrollListener()
    }

    private fun initSearchView() {
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                startNewSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        ibSearch.setOnClickListener {
            startNewSearch(searchView.query?.toString())
        }
    }

    fun startNewSearch(query: String?) {
        if (!query.isNullOrEmpty()) {
            moviesAdapter.clear()
            viewModel.setCurrentSearchText(query)
            viewModel.fetchMovies(searchView.query.toString())
        }
    }

    private fun subscribeUi() {
        viewModel.movieList.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    val list = result.data?.Search
                    if (list.isNullOrEmpty()) {
                        tvNoItem.visibility = View.VISIBLE
                    } else {
                        tvNoItem.visibility = View.GONE
                        moviesAdapter.updateData(list)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun showError(msg: String) {
        Snackbar.make(vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }


    private fun initScrollListener() {
        rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) { // only when scrolling up
                    val visibleThreshold = 2
                    val layoutManager = rvMovies.layoutManager as GridLayoutManager
                    val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    val currentTotalCount = layoutManager.itemCount
                    val nextPage = currentTotalCount / 10 + 1
                    if (currentTotalCount == viewModel.totalMovie.value) return
                    if (currentTotalCount <= lastItem + visibleThreshold) {
                        //show your loading view
                        // load content in background
                        viewModel.fetchMovies(viewModel.currentSearchText.value.orEmpty(), nextPage)
                    }
                }
            }
        })
    }
/*
    private fun loadMore() {
        rowsArrayList.add(null)
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1)
        val handler = Handler()
        handler.postDelayed(Runnable {
            rowsArrayList.remove(rowsArrayList.size() - 1)
            val scrollPosition: Int = rowsArrayList.size()
            recyclerViewAdapter.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                rowsArrayList.add("Item $currentSize")
                currentSize++
            }
            recyclerViewAdapter.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }*/
}