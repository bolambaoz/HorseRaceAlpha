package com.horserace.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.horserace.data.db.entity.HorseNews
import com.horserace.data.db.entity.HorseVideo
import com.horserace.data.db.entity.User
import com.horserace.data.db.horse.HorseDao
import com.horserace.data.db.horse.HorseNewDao
import com.horserace.data.db.user.UserDao

@Database(
    entities = [
        User::class,
        HorseVideo::class,
        HorseNews::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract  fun getUserDao() : UserDao
    abstract  fun getHorseDao() : HorseDao
    abstract fun getHorseHewsDao() : HorseNewDao

    companion object{

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?:buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MyDatabase.db"
            ).build()
    }
}