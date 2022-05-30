package com.chamika.starwarsplanets.model

// Model Planet
data class Planets(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)