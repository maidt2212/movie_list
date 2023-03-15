package io.buildwithnd.demotmdb.ui.listing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MovieRepository
import com.example.myapplication.model.MovieList
import com.example.myapplication.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
/**
 * ViewModel for ListingActivity
 */
class ListingViewModel @Inject constructor(private val movieRepository: MovieRepository) :
        ViewModel() {

    private val _movieList = MutableLiveData<Result<MovieList?>>()
    val movieList = _movieList

    private val _totalMovie = MutableLiveData<Int>()
    val totalMovie = _totalMovie

    private val _currentSearchText = MutableLiveData<String>()
    val currentSearchText = _currentSearchText

    public fun fetchMovies(queryText: String, page: Int? = null) {
        viewModelScope.launch {
            movieRepository.searchMovie(queryText, page).collect {
                _movieList.value = it
                _totalMovie.value= it?.data?.totalResults
            }
        }
    }

    fun setCurrentSearchText(searchText : String){
        _currentSearchText.value = searchText
    }
}