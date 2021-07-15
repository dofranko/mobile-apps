package com.example.l3z1_4

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNewTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)
        title = "Nowy task"
        var spinner = findViewById<Spinner>(R.id.spinnerImportance)
        var pr = resources.getStringArray(R.array.ImportanceList)
        spinner.adapter = ArrayAdapter(this, R.layout.spinner_list_layout, pr)

        spinner = findViewById<Spinner>(R.id.spinnerCategorySelect)
        val dropDownList: ArrayList<SpinnerWithImageModel> = ArrayList()
        for ((key, value) in Globals.categoryBinding.entries) {
            dropDownList.add(SpinnerWithImageModel(key, value))
        }
        spinner.adapter = SpinnerDropDownMenuAdapter(this, dropDownList)

    }


    fun pickTimeOnClick(view: View) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            findViewById<TextView>(R.id.textViewTime)?.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

    }

    fun pickDateOnClick(view: View) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.YEAR, year)
            findViewById<TextView>(R.id.textViewDate)?.text = SimpleDateFormat("yyyy/MM/dd").format(cal.time)
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()

    }

    fun onSaveClick(view: View) {
        val newTask = TaskModel()
        var pickedTime = findViewById<TextView>(R.id.textViewTime).text.toString()
        var pickedDate = findViewById<TextView>(R.id.textViewDate).text.toString()

        newTask.Title = findViewById<EditText>(R.id.editTextTitle).text.toString()
        if (newTask.Title == "") {
            Toast.makeText(this, "Pole tytułu jest wymagane", Toast.LENGTH_LONG).show()
            return
        }
        newTask.Description = findViewById<EditText>(R.id.editTextDescription).text.toString()

        newTask.Importance = findViewById<Spinner>(R.id.spinnerImportance).selectedItem.toString()

        newTask.Image = (findViewById<Spinner>(R.id.spinnerCategorySelect).selectedItem as SpinnerWithImageModel).image
        if (pickedDate == "" && pickedTime == "")
            newTask.IsDateTimed = false
        else if (pickedDate == "" || pickedTime == "") {
            Toast.makeText(this, "Oba pola daty i czasu muszą być uzupełnione lub oba puste", Toast.LENGTH_LONG).show()
            return
        } else {
            SimpleDateFormat("yyyy/MM/dd HH:mm").parse("$pickedDate $pickedTime").also { newTask.Date = it }
            newTask.DateString = "$pickedDate $pickedTime"
        }
        val myintent = Intent()
        myintent.putExtra("newTask", newTask)
        setResult(RESULT_OK, myintent)
        finish()
    }

    fun clearDateOnClick(view: View) {
        findViewById<TextView>(R.id.textViewDate)?.text = ""
    }

    fun clearTimeOnClick(view: View) {
        findViewById<TextView>(R.id.textViewTime)?.text = ""
    }
}