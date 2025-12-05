package com.arslan.reeltime.model

import java.io.Serializable

data class TicketData(
    val id: String? = null,
    val filmTitle: String? = null,
    val seatIds: String? = null,
    val totalPrice: Double? = null,
    val poster: String? = null,
    val purchaseTimestamp: Long? = System.currentTimeMillis(),
    val date: String? = "N/A", // Placeholder
    val time: String? = "N/A" // Placeholder
) : Serializable
