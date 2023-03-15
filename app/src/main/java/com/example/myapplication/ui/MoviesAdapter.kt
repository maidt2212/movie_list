package com.example.myapplication.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.model.Movie
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MoviesAdapter(private val context: Context, private val list: ArrayList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val context: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {

            itemView.tvTitle.text = movie.Title
            Glide.with(context).load(movie.Poster)
                .apply(
                    RequestOptions().override(400, 400).centerInside()
                        .placeholder(R.drawable.no_image)
                ).into(itemView.ivPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return MovieViewHolder(context, view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<Movie>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }
}