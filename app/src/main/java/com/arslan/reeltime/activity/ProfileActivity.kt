package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arslan.reeltime.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Get the current user and display their email
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.emailTxt.text = currentUser.email
        } else {
            // This should not happen if the user is logged in, but as a fallback:
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Handle the logout button click
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            // Clear the activity stack so the user can't go back to the profile screen
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}