package com.example.l3z1_4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class SpinnerDropDownMenuAdapter(val context: Context, val dataSource: ArrayList<SpinnerWithImageModel>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



    private class ItemHolder(row: View?) {
        val labelTextView: TextView
        val iconImageView: ImageView

        init {
            labelTextView = row?.findViewById(R.id.textViewCategoryText) as TextView
            iconImageView = row?.findViewById(R.id.imageViewCategoryIcon) as ImageView
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_with_image_list_layout, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.labelTextView.text = dataSource[position].name
        vh.iconImageView.setBackgroundResource(dataSource[position].image)

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }



}