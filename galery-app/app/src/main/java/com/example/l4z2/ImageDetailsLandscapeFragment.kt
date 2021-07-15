package com.example.l4z2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ImageDetailsLandscapeFragment : Fragment() {

     var rating: Float = 0.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_image_details_landscape, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity?.intent != null){
            val img = activity!!.intent.extras?.getInt("img", 0) ?: 0
            val description = activity!!.intent.extras?.getString("description") ?: "Brak"
            val rating = activity!!.intent.extras?.getFloat("rating", 0.0f) ?: 0
            this.rating = rating.toFloat()
            setViewDisplay(img, description, this.rating)
        }
        val buttonSave = view!!.findViewById<Button>(R.id.buttonSaveL)
        buttonSave.setOnClickListener {
            val myintent = Intent()
            myintent.putExtra("rating", this.rating)
            activity?.setResult(AppCompatActivity.RESULT_OK, myintent)
            activity?.finish()
        }
    }

    fun setViewDisplay(img:Int, description:String, rating:Float){
        val imgView = view!!.findViewById<ImageView>(R.id.imageViewFullSize_L)
        val descView = view!!.findViewById<TextView>(R.id.textViewDescription_L)
        val ratingView = view!!.findViewById<RatingBar>(R.id.ratingBar_L)
        ratingView.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            this.rating = rating
        }
        imgView.setImageResource(img)
        descView.text = description
        ratingView.rating = rating
    }

    fun updateRatingBar(rating: Float){
        val ratingView = view!!.findViewById<RatingBar>(R.id.ratingBar_L)
        ratingView.rating = rating
        this.rating = rating
    }

}