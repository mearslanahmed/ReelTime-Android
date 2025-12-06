package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.arslan.reeltime.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener {
            val name = binding.nameEdt.text.toString().trim()
            val email = binding.emailEdt.text.toString().trim()
            // Correctly get text from TextInputEditText inside TextInputLayout
            val password = binding.passwordEdt.text.toString()
            val confirmPassword = binding.confirmPasswordEdt.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.registerBtn.visibility = View.INVISIBLE

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()

                                user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                                    binding.progressBar.visibility = View.GONE
                                    binding.registerBtn.visibility = View.VISIBLE
                                    if (profileTask.isSuccessful) {
                                        // Registration and profile update success, navigate to main activity
                                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finishAffinity() // Clear all previous activities
                                    }
                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                binding.registerBtn.visibility = View.VISIBLE
                                // If sign up fails, display a message to the user.
                                Toast.makeText(
                                    baseContext, "Authentication failed: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}