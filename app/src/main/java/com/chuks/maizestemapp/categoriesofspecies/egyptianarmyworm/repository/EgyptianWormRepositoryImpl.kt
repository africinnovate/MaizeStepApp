package com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.repository

import androidx.lifecycle.LiveData
import com.chuks.maizestemapp.capturedinsect.dao.InsectDao
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.repository.AfricanArmyWormRepository
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.dao.EgyptianArmyWormDao
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.data.remote.MaizeInsectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

/**
 * This is EgyptianWormRepositoryImpl class. This class takes in two params [maizeInsectApi]
 * and [egyptianArmyWormDao] and implements [AfricanArmyWormRepository]
 * */
class EgyptianWormRepositoryImpl(private val insectApi: MaizeInsectApi,
                                 private val egyptianArmyWormDao: InsectDao) : EgyptianWormRepository {

    /**
     * This [getEgyptianArmyWorm] and return a liveData of Insects
     * */
    override fun getEgyptianArmyWorm(name:String): LiveData<List<Insect>> {
        return egyptianArmyWormDao.getInsectByName(name)
    }

    /**
     * This [requestEgyptianArmyWorm] is a suspend function which must be called from a coroutine
     * */
    override suspend fun requestEgyptianArmyWorm() = withContext(Dispatchers.IO) {
        val response = insectApi.getEgyptianArmyWorm()
        try{
            if (response.isSuccessful){
                val egyptainList = response.body()
                egyptianArmyWormDao.insertInsects(egyptainList)

            }else{
                Timber.i("request failed due to ${response.errorBody()?.string()}")
            }
        }catch (e: Exception){
            Timber.i("Throw an exception ${e.message}")
        }
    }

    override suspend fun deleteInsect() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllInsect() {
        TODO("Not yet implemented")
    }
}