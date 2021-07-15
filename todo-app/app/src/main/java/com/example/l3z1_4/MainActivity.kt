
package com.example.l3z1_4

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.l3z1_4.Database.TaskEntity
import com.example.l3z1_4.Database.TodoDatabase
import kotlin.concurrent.thread

/**
 * Nie polecam czytać kodu
 */
class MainActivity : AppCompatActivity() {

    val FullTaskList: ArrayList<TaskModel> = ArrayList<TaskModel>()
    var FilteredTaskList: ArrayList<TaskModel> = ArrayList<TaskModel>()
    val adapter: CustomAdapter = CustomAdapter(FilteredTaskList, this)
    var dateFilter: String = ""

    lateinit var db: TodoDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "ToDo'sy"
        createNotificationChannel()
        val recycler = findViewById<RecyclerView>(R.id.RecyclerViewMy)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeToCheck(adapter))
        itemTouchHelper.attachToRecyclerView(recycler)
        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleDataSetAndOptions()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        val spinner = findViewById<Spinner>(R.id.spinnerSort)
        val arrli = arrayOf("Data: rosnąco", "Data: malejąco", "Priorytet")
        spinner.adapter = ArrayAdapter(this, R.layout.spinner_list_layout, arrli)
        spinner.onItemSelectedListener = onItemSelectedListener

        val spinner2 = findViewById<Spinner>(R.id.spinnerCategory)
        val pr = arrayOf("Wszystkie") + resources.getStringArray(R.array.CategoryList)
        spinner2.adapter = ArrayAdapter(this, R.layout.spinner_list_layout, pr)
        spinner2.onItemSelectedListener = onItemSelectedListener

        handleDataSetAndOptions()
    }

    fun handleDataSetAndOptions() {
        getTasksFromDb()
        FilteredTaskList.clear()
        var copyArray: ArrayList<TaskModel> = FullTaskList.clone() as ArrayList<TaskModel>
        val selectedCategory = findViewById<Spinner>(R.id.spinnerCategory).selectedItem.toString()
        val selectedSort = findViewById<Spinner>(R.id.spinnerSort).selectedItem.toString()
        if (dateFilter != "") {
            copyArray =
                copyArray.filter { x -> x.DateString.startsWith(dateFilter) } as ArrayList<TaskModel>
        }
        when (selectedCategory) {
            "Wszystkie" -> null
            else -> {
                copyArray =
                    copyArray.filter { x -> x.Image == Globals.categoryBinding[selectedCategory] } as ArrayList<TaskModel>
            }
        }
        when (selectedSort) {
            "Data: rosnąco" -> copyArray.sortBy { x -> x.DateString }
            "Data: malejąco" -> copyArray.sortByDescending { x -> x.DateString }
            "Priorytet" -> copyArray.sortBy { x -> x.Priority }
        }
        FilteredTaskList.clear()
        FilteredTaskList.addAll(copyArray)
        adapter.notifyDataSetChanged()
    }

    fun pupUpCalendarButtonOnClick(view: View) {
        val events: HashMap<String, Int> = HashMap()
        FullTaskList.sortedByDescending { x -> x.Priority }
            .filter { x -> !x.IsDone && x.DateString != "" }
            .forEach {
                events[it.DateString] = when (it.Priority) {
                    0 -> Color.RED
                    1 -> Color.BLUE
                    2 -> Color.GREEN
                    else -> Color.YELLOW
                }
            }
        val intent = Intent(this, PopUpCalendarActivity::class.java)
        intent.putExtra("events", events)
        startActivityForResult(intent, 1)
    }

    fun buttonNewItemOnClick(view: View) {
        var intent = Intent(this, AddNewTask::class.java)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val result = data?.getSerializableExtra("newTask") as TaskModel
            val newId = insertNewTaskToDb(result)
            findViewById<Spinner>(R.id.spinnerSort).setSelection(0)
            findViewById<Spinner>(R.id.spinnerCategory).setSelection(0)
            handleDataSetAndOptions()

            //Ustawianie powiadomien
            if(result.IsDateTimed) {
                val intent = Intent(this, MyReminderBroadcast::class.java)
                intent.putExtra("title", result.Title)
                intent.putExtra("id", newId)
                intent.putExtra("image", result.Image)
                val pendingIntent = PendingIntent.getBroadcast(this, newId.toInt(), intent, 0)
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, result.Date.time - 15 * 60 * 1000, pendingIntent)
            }


        }else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("dateFilter")
            if (result != null) {
                dateFilter = result
            }
            handleDataSetAndOptions()
        }else if (requestCode == 2) {
            Toast.makeText(this, "reminder", Toast.LENGTH_LONG).show()
        }
    }

    fun insertNewTaskToDb(task : TaskModel) : Long{
        checkIfDatabase()
        var returnId: Long = 0
        val t = thread {
            val toSave = TaskEntity(
                    id = task.id,
                    DateString = task.DateString,
                    Description = task.Description,
                    Title = task.Title,
                    IsDateTimed = task.IsDateTimed,
                    Priority = task.Priority,
                    Image = task.Image,
                    IsDone = task.IsDone
            )
            returnId = db.tasksDao().insertOne(toSave)
        }
        t.join()
        handleDataSetAndOptions()
        return returnId
    }

    fun saveTasksToDb() {
        checkIfDatabase()
        val t = thread {
            val toSave = FullTaskList.map { x ->
                TaskEntity(
                    id = x.id,
                    DateString = x.DateString,
                    Description = x.Description,
                    Title = x.Title,
                    IsDateTimed = x.IsDateTimed,
                    Priority = x.Priority,
                    Image = x.Image,
                    IsDone = x.IsDone
                )
            }.toTypedArray()
            db.tasksDao().insertAll(*toSave)
        }
        t.join()
        handleDataSetAndOptions()
    }

    fun removeTask(task: TaskModel) {
        checkIfDatabase()
        task.IsDone = true
        updateAlarmOnCheckBox(task)
        val t = thread {
            db.tasksDao().delete(
                TaskEntity(
                    id = task.id,
                    DateString = task.DateString,
                    Description = task.Description,
                    Title = task.Title,
                    IsDateTimed = task.IsDateTimed,
                    Priority = task.Priority,
                    Image = task.Image,
                    IsDone = task.IsDone
                )
            )
        }
        t.join()
        handleDataSetAndOptions()
    }

    private fun getTasksFromDb() {
        checkIfDatabase()
        val t = thread {
            val tasks = db.tasksDao().getAll()
            val toSave = tasks.map { x ->
                TaskModel(
                    x.id, x.DateString, x.Description, x.Title, x.IsDateTimed,
                    x.Priority, x.Image, x.IsDone
                )
            }
            FullTaskList.clear()
            for (task in toSave) {
                FullTaskList.add(task)
            }
        }
        t.join()
    }

    private fun checkIfDatabase() {
        if (!this::db.isInitialized || db == null)
            db = TodoDatabase.getInstance(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        handleDataSetAndOptions()
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "MyReminderChannel"
            val description = "Channel for my reminder task"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifyTask", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun updateAlarmOnCheckBox(task : TaskModel){
        val intent = Intent(this, MyReminderBroadcast::class.java)
        intent.putExtra("title", task.Title)
        intent.putExtra("id", task.id)
        intent.putExtra("image", task.Image)

        val pendingIntent = PendingIntent.getBroadcast(this, task.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        when(task.IsDone) {
            true -> alarmManager.cancel(pendingIntent)
            false -> alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.Date.time - 15 * 60 * 1000, pendingIntent)
        }

    }

}