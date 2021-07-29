package com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.repository

import androidx.lifecycle.LiveData
import com.chuks.maizestemapp.capturedinsect.dao.InsectDao
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.repository.AfricanArmyWormRepository
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.dao.FallArmyWormDao
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.data.remote.MaizeInsectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

/**
 * This is FallArmyWormRepositoryImpl class. This class takes in two params [maizeInsectApi]
 * and [fallArmyWormDao] and implements [AfricanArmyWormRepository]
 * */
class FallArmyWormRepositoryImpl(
    private val insectApi: MaizeInsectApi,
    private val fallArmyWormDao: InsectDao
) : FallArmyWormRepository {

    /**
     * This [getFallArmyWorm] and return a liveData of Insects
     * */
    override fun getFallArmyWorm(name: String): LiveData<List<Insect>> {

        return fallArmyWormDao.getInsectByName(name)
    }

    /**
     * This [requestFallArmyWorm] is a suspend function which must be called from a coroutine
     * */
    override suspend fun requestFallArmyWorm() = withContext(Dispatchers.IO) {
        val response = insectApi.getFallArmyArmyWorm()
        try {
            if (response.isSuccessful) {
                val fallArmyList = response.body()
                fallArmyWormDao.insertInsects(fallArmyList)
            } else {
                Timber.i("request failed due to ${response.errorBody()?.string()}")
            }

        } catch (e: Exception) {
            Timber.i("Throw an exception ${e.message}")
        }

    }

    override suspend fun deleteInsect() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllBYName(name: String) {
        val response = insectApi.deleteAllInsect()
        try {
            if(response.isSuccessful){
                fallArmyWormDao.deleteAllByName(name)
                Timber.d("response $response")
                Timber.d("deleted item ${response.body()}")
            }
        }catch (e : Exception){
            Timber.e(e)
        }
    }
    }
