package com.example.l4z2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout
    private val imageArrayList : ArrayList<Int> = ArrayList()
    private val imageNameDict : LinkedHashMap<Int, String> = LinkedHashMap()
    private val imageStarsDict : LinkedHashMap<Int, Float> = LinkedHashMap()
    private val imageDescDict : LinkedHashMap<Int, String> = LinkedHashMap()
    private var lastImage : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Cat Gallery"
        populateImageArrayList()

        viewPager = findViewById(R.id.viewPager2Main)
        viewPager.adapter = GalleryRecyclerAdapter(imageArrayList, imageStarsDict, imageDescDict,this)
        tabLayout = findViewById(R.id.tabLayoutMain)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, viewPager){ tab: TabLayout.Tab, position: Int ->
            //tab.setCustomView(R.layout.tablayout_item_view)
            tab.text = position.toString()
            tab.setIcon(imageArrayList[position])
        }.attach()
        //recyclerView = findViewById(R.id.recyclerMainGalerry)
        //recyclerView.layoutManager = GridLayoutManager(this, 3)
        //recyclerView.adapter = GalleryRecyclerAdapter(imageArrayList,this)

    }

    private fun populateImageArrayList(){
        for(i in 1..21){
            var name : String =  i.toString()
            if(i < 10){
                name = "0$name"
            }
            name = "img$name"
            val img = resources.getIdentifier(name, "drawable", packageName)
            if(img ==0){
                continue
            }
            imageArrayList.add(img)
            imageNameDict[img] = name
            imageStarsDict[img] = 5.0f
            val descriptions = resources.getStringArray(R.array.descriptions)
            for(desc in descriptions){
                if (desc.split(":")[0] == imageNameDict[img]){
                    imageDescDict[img] = desc.split(":")[1]
                    break
                }
            }

        }

    }

    fun openImageDetails(img:Int){
        return;
        val descriptions = resources.getStringArray(R.array.descriptions)
        var description = ""
        for(desc in descriptions){
            if (desc.split(":")[0] == imageNameDict[img]){
                description = desc.split(":")[1]
            }
        }
        if(description == "") return
        val rating = imageStarsDict[img]
        val intent = Intent(this,  ImageDetailsActivity::class.java)
        lastImage = img
        intent.putExtra("img", img)
        intent.putExtra("description", description)
        intent.putExtra("rating", rating)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){
            if (data != null) {
                imageStarsDict[lastImage] = data.getFloatExtra("rating", 0.0f)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("imageArrayList", this.imageArrayList)
        outState.putSerializable("imageNameDict", this.imageNameDict)
        outState.putSerializable("imageStarsDict", this.imageStarsDict)
        outState.putSerializable("imageDescDict", this.imageDescDict)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.imageArrayList.addAll(savedInstanceState.getSerializable("imageArrayList") as ArrayList<Int>)
        this.imageNameDict.putAll(savedInstanceState.getSerializable("imageNameDict") as LinkedHashMap<Int, String>)
        this.imageStarsDict.putAll(savedInstanceState.getSerializable("imageStarsDict") as LinkedHashMap<Int, Float>)
        this.imageDescDict.putAll(savedInstanceState.getSerializable("imageDescDict") as LinkedHashMap<Int, String>)
    }

}