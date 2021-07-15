package com.example.l5z1


import android.content.Context
import androidx.room.*

@Entity(tableName = "Results")
data class ResultEntity (
    @PrimaryKey(autoGenerate = true) val id : Int,
    var PlayerScore : Int,
    var EnemyScore : Int
)

@Dao
interface ResultsDao {
    @Query("SELECT * FROM Results")
    fun getAll(): List<ResultEntity>

    @Query("SELECT  * FROM Results order by id desc limit 10")
    fun getTop(): List<ResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: ResultEntity)

}

@Database(entities = arrayOf(ResultEntity::class), version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultsDao

    companion object{
        @Volatile
        private var instance : GameDatabase? = null
        fun getInstance(context: Context): GameDatabase{
            return instance ?: synchronized(this){
                instance
                    ?: buildDatabase(context).also {instance = it}
            }
        }
        private fun buildDatabase(context: Context): GameDatabase{
            return Room.databaseBuilder(context, GameDatabase::class.java, "GameDatabase")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}