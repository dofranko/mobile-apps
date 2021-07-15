package com.example.l4z2

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ImageDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var savedRating : Float = -1.0f
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            val frag : ImageDetailsPortraitFragment = supportFragmentManager.findFragmentById(R.id.fragmentPortrait) as ImageDetailsPortraitFragment
            savedRating = frag.rating
        }
        else if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val frag : ImageDetailsLandscapeFragment = supportFragmentManager.findFragmentById(R.id.fragmentLandscape) as ImageDetailsLandscapeFragment
            savedRating = frag.rating
        }

        outState.putFloat("rating-saved", savedRating)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        var savedRating = savedInstanceState.getFloat("rating-saved")
        if (savedRating == -1.0f) return

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            val frag : ImageDetailsPortraitFragment = supportFragmentManager.findFragmentById(R.id.fragmentPortrait) as ImageDetailsPortraitFragment
            frag.updateRatingBar(savedRating)
        }
        else if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val frag : ImageDetailsLandscapeFragment = supportFragmentManager.findFragmentById(R.id.fragmentLandscape) as ImageDetailsLandscapeFragment
            frag.updateRatingBar(savedRating)
        }
    }
}