package com.arslan.reeltime.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.activity.TicketActivity
import com.arslan.reeltime.databinding.ViewholderTicketBinding
import com.arslan.reeltime.model.TicketData
import com.bumptech.glide.Glide

class MyTicketsAdapter(private val tickets: List<TicketData>) :
    RecyclerView.Adapter<MyTicketsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: TicketData) {
            Glide.with(binding.root.context)
                .load(ticket.poster)
                .into(binding.filmPoster)

            binding.filmTitle.text = ticket.filmTitle
            binding.filmDate.text = ticket.date
            binding.filmTime.text = ticket.time
            binding.filmSeats.text = ticket.seatIds
            binding.filmPrice.text = "$${ticket.totalPrice}"

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, TicketActivity::class.java)
                intent.putExtra("filmTitle", ticket.filmTitle)
                intent.putExtra("seatIds", ticket.seatIds)
                intent.putExtra("totalPrice", ticket.totalPrice)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderTicketBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount(): Int = tickets.size
}