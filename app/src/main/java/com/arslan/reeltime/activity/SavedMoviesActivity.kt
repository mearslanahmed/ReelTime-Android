package com.arslan.reeltime.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()
        binding = ActivitySavedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the adapter with the unsave callback
        adapter = SavedMoviesAdapter(savedMovies) { film ->
            unsaveMovie(film)
        }
        binding.savedMoviesRecyclerView.adapter = adapter
        binding.savedMoviesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the back button
        binding.backBtn.setOnClickListener {
            finish()
        }

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
                // Handle database error
                Toast.makeText(this@SavedMoviesActivity, "Failed to load movies.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun unsaveMovie(film: Film) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val savedMoviesRef = FirebaseDatabase.getInstance().getReference("saved_movies").child(userId)

        film.Title?.let { title ->
            // Sanitize the title to create a valid Firebase key
            val sanitizedTitle = title.replace(Regex("[.#$\\[\\]/]"), "")
            if (sanitizedTitle.isBlank()) {
                Toast.makeText(this, "Cannot unsave movie with invalid title.", Toast.LENGTH_SHORT).show()
                return@let
            }

            savedMoviesRef.child(sanitizedTitle).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Movie Unsaved!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to unsave movie.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
