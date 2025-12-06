package com.arslan.reeltime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.databinding.ViewholderSavedMovieBinding
import com.arslan.reeltime.model.Film
import com.bumptech.glide.Glide

class SavedMoviesAdapter(private val movies: List<Film>, private val onUnsaveClicked: (Film) -> Unit) : RecyclerView.Adapter<SavedMoviesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderSavedMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            binding.filmTitle.text = film.Title
            Glide.with(binding.root.context)
                .load(film.Poster)
                .into(binding.filmPoster)
            binding.unsaveBtn.setOnClickListener { onUnsaveClicked(film) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderSavedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size
}