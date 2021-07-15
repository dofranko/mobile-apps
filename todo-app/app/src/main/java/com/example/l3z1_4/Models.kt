package com.example.l3z1_4

import android.icu.text.SimpleDateFormat
import java.io.Serializable
import java.lang.Exception
import java.util.*

class TaskModel : Serializable {
    var id : Int = 0
    var Date : Date = Date()
    var DateString  = ""
    var Description : String = ""
    var Title : String = ""
    var IsDateTimed : Boolean = true
    var Importance : String = ""
    set(value) {
        field = value
        Priority = when(value.toLowerCase()){
            "wysoki" -> 0
            "średni" -> 1
            "niski" -> 2
            else -> 3
        }
    }
    var Priority : Int  = 5
    var Image : Int = 0
    var IsDone = false

    constructor(){}
    constructor(id : Int, dateString : String, description : String, title : String, isDateTimed : Boolean,
    priority : Int, image : Int, isDone: Boolean){
        this.id = id
        this.DateString = dateString
        this.Description = description
        this.Title = title
        this.IsDateTimed = isDateTimed
        if(isDateTimed) {
            try {
                this.Date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(this.DateString)
            }
            catch(e : Exception){}
        }
        this.Priority = priority
        this.Importance = when(priority){
            0 -> "Wysoki"
            1 -> "Średni"
            2-> "Niski"
            else -> "Brak"
        }
        this.IsDone = isDone
        this.Image = image
    }

}

class SpinnerWithImageModel(val name: String, val image: Int) : Serializable {


}