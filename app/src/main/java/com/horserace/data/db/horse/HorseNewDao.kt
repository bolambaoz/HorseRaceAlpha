package com.horserace.data.db.horse

import androidx.lifecycle.LiveData
import androidx.room.*
import com.horserace.data.db.entity.HorseNews
import com.horserace.data.db.entity.HorseVideo

@Dao
interface HorseNewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savaAllHorseNews(horseVideo: List<HorseNews>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAllHorse(horseVideo: List<HorseNews>)

    @Query("SELECT * FROM horse_news")
    fun getHorseNews() : LiveData<List<HorseNews>>
}