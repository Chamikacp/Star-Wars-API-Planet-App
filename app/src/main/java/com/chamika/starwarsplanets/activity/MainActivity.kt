package com.chamika.starwarsplanets.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chamika.starwarsplanets.R
import com.chamika.starwarsplanets.SWPlanetsInterface
import com.chamika.starwarsplanets.adapter.PlanetAdapter
import com.chamika.starwarsplanets.model.Planets
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://swapi.dev/api/"

class MainActivity : AppCompatActivity() {

    lateinit var planetAdapter: PlanetAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    private var page: Int = 1
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // start theme song
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.theme)
        mediaPlayer.start()
        mediaPlayer.isLooping = true

        // get all planets for page 1
        getAllPlanets(1)

        // sound on or off button click listener
        soundButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                soundButton.setImageResource(R.drawable.mute)
            } else {
                mediaPlayer.start()
                soundButton.setImageResource(R.drawable.sound)
            }
        }

        // next page button click listener
        nextBtn.setOnClickListener {
            loading_gif.isVisible = true
            page++
            getAllPlanets(page)
        }

        // previous button button click listener
        previousBtn.setOnClickListener {
            loading_gif.isVisible = true
            page--
            getAllPlanets(page)
        }
    }

    // Get app available planets from API
    private fun getAllPlanets(page: Int) {

        val recyclerView: RecyclerView = findViewById(R.id.planetRecyclerView)

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SWPlanetsInterface::class.java)

        val retrofitData = retrofitBuilder.getPlanets(page)
        retrofitData.enqueue(object : Callback<Planets?> {
            override fun onResponse(call: Call<Planets?>, response: Response<Planets?>) {
                val responseBody = response.body()!!.results

                planetAdapter = PlanetAdapter(baseContext, responseBody)
                planetAdapter.notifyDataSetChanged()

                recyclerView.setHasFixedSize(true)
                gridLayoutManager = GridLayoutManager(baseContext, 2)
                recyclerView.layoutManager = gridLayoutManager
                recyclerView.adapter = planetAdapter

                // recycler view on click listener
                planetAdapter.setOnClickListener(object : PlanetAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {

                        // show bottom sheet dialog
                        val bottomSheetDialog =
                            BottomSheetDialog(this@MainActivity, R.style.BottomSheetDialogTheme)
                        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                            R.layout.bottom_sheet,
                            findViewById<ConstraintLayout>(R.id.bottomSheet)
                        )

                        val name: TextView = bottomSheetView.findViewById(R.id.name)
                        val image: ImageView = bottomSheetView.findViewById(R.id.image)
                        val orbitalPeriod: TextView =
                            bottomSheetView.findViewById(R.id.orbitalPeriod)
                        val gravity: TextView = bottomSheetView.findViewById(R.id.gravity)
                        val terrain: TextView = bottomSheetView.findViewById(R.id.terrain)
                        val population: TextView = bottomSheetView.findViewById(R.id.population)

                        name.text = responseBody[position].name
                        image.setImageResource(planetAdapter.imageArray[position])
                        orbitalPeriod.text = responseBody[position].orbital_period
                        gravity.text = responseBody[position].gravity
                        terrain.text = responseBody[position].terrain
                        population.text = responseBody[position].population

                        bottomSheetDialog.setContentView(bottomSheetView)
                        bottomSheetDialog.show()
                    }
                })
                loading_gif.isVisible = false

            }

            override fun onFailure(call: Call<Planets?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: " + t.message)
            }

        })

        nextBtn.isEnabled = page != 6
        previousBtn.isEnabled = page != 1
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

}