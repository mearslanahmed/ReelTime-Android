package com.arslan.reeltime.model

import java.io.Serializable

data class Film(
    var Title: String? = null,
    var Description: String? = null,
    var Poster: String? = null,
    var Time: String? = null,
    var Trailer: String? = null,
    var Imdb: Double? = null,
    var Year: Int? = null,
    var Price: Double? = null,
    var Genre: ArrayList<String> = ArrayList(),
    var Casts: ArrayList<Cast> = ArrayList()
    ): Serializable
