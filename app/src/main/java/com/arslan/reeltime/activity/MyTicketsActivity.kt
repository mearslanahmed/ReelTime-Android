package com.arslan.reeltime.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arslan.reeltime.adapter.MyTicketsAdapter
import com.arslan.reeltime.databinding.ActivityMyTicketsBinding
import com.arslan.reeltime.model.TicketData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyTicketsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyTicketsBinding
    private lateinit var adapter: MyTicketsAdapter
    private val tickets = mutableListOf<TicketData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ticketsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyTicketsAdapter(tickets)
        binding.ticketsRecyclerView.adapter = adapter

        loadTicketsFromFirebase()
    }

    private fun loadTicketsFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("tickets").child(userId)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tickets.clear()
                for (ticketSnapshot in snapshot.children) {
                    val ticket = ticketSnapshot.getValue(TicketData::class.java)
                    if (ticket != null) {
                        tickets.add(ticket)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
