package com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.repository

import androidx.lifecycle.LiveData
import com.chuks.maizestemapp.common.data.Insect

/**
 * This is EgyptianWormRepository Interface
 * */
interface EgyptianWormRepository {
    /**
     * This [getEgyptianArmyWorm] and return a liveData of Insects
     * */
    fun getEgyptianArmyWorm(name:String) : LiveData<List<Insect>>

    /**
     * This [requestAfricanArmyWorm] and insert into the database
     * */
    suspend fun requestEgyptianArmyWorm()

    // This will delete items from the database
    suspend fun deleteInsect()

    // This will delete all items from the database
    suspend fun deleteAllInsect()
}