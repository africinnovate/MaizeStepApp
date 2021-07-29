package com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.repository

import androidx.lifecycle.LiveData
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.data.InsectModel

/**
 * This is AfricanArmyWormRepository Interface
 * */
interface AfricanArmyWormRepository {
    /**
     * This [getAfricanArmyWorm] and return a liveData of Insects
     * */
    fun getAfricanArmyWorm(name : String) : LiveData<List<Insect>>

    /**
     * This [requestAfricanArmyWorm] and insert into the database
     * */
    suspend fun requestAfricanArmyWorm()

    // This will delete items from the database
    suspend fun deleteInsect()

    // This will delete all items from the database
    suspend fun deleteAllInsect()

}