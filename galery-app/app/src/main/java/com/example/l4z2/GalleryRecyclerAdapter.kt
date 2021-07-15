package com.example.l4z2


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class GalleryRecyclerAdapter(private val imgSet: ArrayList<Int>,
                             private val starsDict: LinkedHashMap<Int, Float>,
                             private val descDict: LinkedHashMap<Int, String>,
                             private val context : Context) :
    RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>() {


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.imageViewRecyclerItem)
        val textView: TextView = view.findViewById(R.id.textViewMainDesc)
        val ratingBar : RatingBar = view.findViewById(R.id.ratingBarMain)
        var image: Int = 0
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.viewpager_item_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val img = imgSet[position]
        viewHolder.imageView.setImageResource(img)
        viewHolder.image = imgSet[position]
        viewHolder.itemView.setOnClickListener {
            (context as MainActivity).openImageDetails(imgSet[position])
        }
        viewHolder.textView.text = descDict[img]
        viewHolder.ratingBar.rating = starsDict[img] ?: 5.0f
        viewHolder.ratingBar.setOnRatingBarChangeListener{ ratingBar, rating, fromUser ->
            starsDict[img] = rating
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = imgSet.size


}
