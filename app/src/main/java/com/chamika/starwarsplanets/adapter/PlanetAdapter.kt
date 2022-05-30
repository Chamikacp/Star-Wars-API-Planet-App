package com.chamika.starwarsplanets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chamika.starwarsplanets.R
import com.chamika.starwarsplanets.model.Result

class PlanetAdapter(private val context: Context, private val planetList: List<Result>) :
    RecyclerView.Adapter<PlanetAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener
    var imageArray: Array<Int> = arrayOf(
        R.drawable.g,
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e,
        R.drawable.f,
        R.drawable.h,
        R.drawable.i,
        R.drawable.j
    )

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class ViewHolder(planetView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(planetView) {
        var planetName: TextView
        var planetClimate: TextView
        var planetImage: ImageView

        init {
            planetName = planetView.findViewById(R.id.planetName)
            planetClimate = planetView.findViewById(R.id.planetClimate)
            planetImage = planetView.findViewById(R.id.planetImage)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    // create view holder for one item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val planetView = LayoutInflater.from(context).inflate(R.layout.row_planet, parent, false)
        return ViewHolder(planetView, mListener)
    }

    // bind data according to the item position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.planetName.text = planetList[position].name
        holder.planetClimate.text = planetList[position].climate
        holder.planetImage.setImageResource(imageArray[position])
    }

    // get count of all available items
    override fun getItemCount(): Int {
        return planetList.size
    }

}