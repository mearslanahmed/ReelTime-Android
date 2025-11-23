package com.arslan.reeltime.model

data class Seat(var status: SeatStatus, var name: String){
    enum class SeatStatus{
        AVAILABLE,
        SELECTED,
        UNAVAILABLE

    }
}
