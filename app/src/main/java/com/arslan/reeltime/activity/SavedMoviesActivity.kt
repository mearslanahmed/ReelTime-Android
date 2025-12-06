package com.arslan.reeltime.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arslan.reeltime.adapter.SavedMoviesAdapter
import com.arslan.reeltime.databinding.ActivitySavedMoviesBinding
import com.arslan.reeltime.model.Film
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SavedMoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedMoviesBinding
    private lateinit var adapter: SavedMoviesAdapter
    private val savedMovies = mutableListOf<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.savedMoviesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SavedMoviesAdapter(savedMovies)
        binding.savedMoviesRecyclerView.adapter = adapter

        loadSavedMovies()
    }

    private fun loadSavedMovies() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val savedMoviesRef = FirebaseDatabase.getInstance().getReference("saved_movies").child(userId)

        savedMoviesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                savedMovies.clear()
                for (movieSnapshot in snapshot.children) {
                    val movie = movieSnapshot.getValue(Film::class.java)
                    movie?.let { savedMovies.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
