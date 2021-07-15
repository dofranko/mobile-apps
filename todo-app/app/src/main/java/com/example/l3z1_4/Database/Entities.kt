package com.example.l3z1_4.Database

import android.content.Context
import androidx.room.*

@Entity(tableName = "Tasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true) val id : Int,
    var DateString : String,
    var Description : String,
    var Title : String,
    var IsDateTimed : Boolean,
    var Priority : Int,
    var Image : Int,
    var IsDone : Boolean
)

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tasks: TaskEntity)

    @Insert
    fun insertOne(task : TaskEntity) : Long

    @Delete
    fun delete(task: TaskEntity)

    @Update
    fun update(task: TaskEntity)
}

@Database(entities = arrayOf(TaskEntity::class), version = 2)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao

    companion object{
        @Volatile
        private var instance : TodoDatabase? = null
        fun getInstance(context: Context): TodoDatabase{
            return instance ?: synchronized(this){
                instance
                    ?: buildDatabase(context).also {instance = it}
            }
        }
        private fun buildDatabase(context: Context): TodoDatabase{
            return Room.databaseBuilder(context, TodoDatabase::class.java, "TodoDatabase")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
