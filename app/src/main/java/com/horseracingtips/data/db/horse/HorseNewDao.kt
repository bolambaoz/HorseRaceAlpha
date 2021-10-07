package com.horseracingtips.data.db.horse

import androidx.lifecycle.LiveData
import androidx.room.*
import com.horseracingtips.data.db.entity.HorseNews

@Dao
interface HorseNewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savaAllHorseNews(horseVideo: List<HorseNews>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAllHorse(horseVideo: List<HorseNews>)

    @Query("SELECT * FROM horse_news")
    fun getHorseNews() : LiveData<List<HorseNews>>
}