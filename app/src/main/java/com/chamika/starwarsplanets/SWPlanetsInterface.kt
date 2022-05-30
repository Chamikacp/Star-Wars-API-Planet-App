package com.chamika.starwarsplanets

import com.chamika.starwarsplanets.model.Planets
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SWPlanetsInterface {

    @GET("planets/?pages=page")
    fun getPlanets(@Query("page") page: Int): Call<Planets>

}