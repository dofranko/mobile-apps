package com.example.l3z1_4

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataSet: ArrayList<TaskModel>, private val context : Context) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var buggyShaderFactory: (width: Float, height: Float) -> Shader = { viewWidth, viewHeight ->
        RadialGradient(
                viewWidth / 2f,
                viewHeight / 2f,
                Math.min(viewWidth, viewHeight) / 2f,
                Color.RED,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
        )
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView
        val textViewDate: TextView
        val textViewDescription: TextView
        val imageViewIcon: ImageView
        val imageViewIconCheckbox: ImageView
        var importance : String
        var isDone : Boolean

        init {
            // Define click listener for the ViewHolder's View.
            textViewTitle = view.findViewById(R.id.textViewTitle)
            textViewDate = view.findViewById(R.id.textViewDate)
            textViewDescription = view.findViewById(R.id.textViewDescription)
            imageViewIcon = view.findViewById(R.id.imageViewIcon)
            imageViewIconCheckbox = view.findViewById(R.id.imageViewCheckbox)
            importance = ""
            isDone = false
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_item_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewDate.text = dataSet[position].DateString
        viewHolder.textViewTitle.text = dataSet[position].Title
        viewHolder.textViewDescription.text = dataSet[position].Description
        viewHolder.imageViewIcon.setBackgroundResource(dataSet[position].Image)
        viewHolder.importance = dataSet[position].Importance
        viewHolder.isDone = dataSet[position].IsDone
        when(viewHolder.isDone) {
            true -> viewHolder.imageViewIconCheckbox.setBackgroundResource(android.R.drawable.checkbox_on_background)
            false -> viewHolder.imageViewIconCheckbox.setBackgroundResource(android.R.drawable.checkbox_off_background)
        }
        val color = when(dataSet[position].Priority){
            0 -> Color.parseColor("#55FF0000")
            1 -> Color.parseColor("#550000FF")
            2 -> Color.parseColor("#5500FF00")
            else -> Color.TRANSPARENT
        }
        val gd = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                        color,
                        Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT,
                        color)
        )
        gd.cornerRadius = 20f
        viewHolder.itemView.background = gd
        viewHolder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Usuń element")
            builder.setMessage("Na pewno usunąć zadanie \"${dataSet[position].Title}\"?")
            builder.setPositiveButton("Tak") { _, _ ->
                (context as MainActivity).removeTask(dataSet[position])
            }
            builder.setNegativeButton("Anuluj") { _, _, -> }
            builder.show()
            true
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun changeStateOfItem(pos : Int){
        dataSet[pos].IsDone = !dataSet[pos].IsDone
        if(dataSet[pos].IsDone) {
            Toast.makeText(context, "Wykonano \"${dataSet[pos].Title}\"", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Przywrócono \"${dataSet[pos].Title}\"", Toast.LENGTH_SHORT).show()
        }
        (context as MainActivity).updateAlarmOnCheckBox(dataSet[pos])
        context.saveTasksToDb()
        notifyItemChanged(pos)
    }

}
