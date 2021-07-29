package com.chuks.maizestemapp.capturedinsect.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chuks.maizestemapp.common.data.Insect

/**
 * This is the Data Access Object (DAO) for captured insect
 * */
@Dao
interface InsectDao {

    /**
     * This is query [getAllCapturedInsect]
     * */
    @Query("SELECT * from insect ORDER BY timeStamp DESC")
    fun getAllCapturedInsect(): LiveData<List<Insect>>

    /**
     * This [insertAllInsect] to the database
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsects(insect: List<Insect>?)

    /**
     * Get all captured insect by their name [getAllCapturedInsect]
     * */
    @Query("SELECT * FROM Insect WHERE name = :name ORDER BY timeStamp DESC")
    fun getInsectByName(name: String): LiveData<List<Insect>>

    @Query("DELETE FROM insect WHERE classPredictionId = :id")
    suspend fun delete(id: String?)

    @Query("DELETE FROM insect")
    suspend fun deleteAll()

    @Query("DELETE FROM insect WHERE name = :name")
    suspend fun deleteAllByName(name : String)

}