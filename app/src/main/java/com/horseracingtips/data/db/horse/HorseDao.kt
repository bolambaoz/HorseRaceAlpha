package com.horseracingtips.data.db.horse
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.horseracingtips.data.db.entity.HorseVideo

@Dao
interface HorseDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savaAllHorseVideo(horseVideo: List<HorseVideo>)

    @Query("SELECT * FROM horse_race")
    fun getHorse() : LiveData<List<HorseVideo>>
}