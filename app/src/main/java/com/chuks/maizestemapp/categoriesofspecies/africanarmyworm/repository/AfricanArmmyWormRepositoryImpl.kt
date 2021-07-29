package com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chuks.maizestemapp.capturedinsect.dao.InsectDao
import com.chuks.maizestemapp.capturedinsect.repository.CapturedInsectRepository
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.dao.AfricanArmyWormDao
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.data.InsectModel
import com.chuks.maizestemapp.common.data.remote.MaizeInsectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

/**
 * This is AfricanArmmyWormRepositoryImpl class. This class takes in two params [maizeInsectApi]
 * and [africanArmyWormDao] and implements [AfricanArmyWormRepository]
 * */
class AfricanArmmyWormRepositoryImpl(
    private val insectApi: MaizeInsectApi,
    private val africanArmyWormDao: InsectDao
) : AfricanArmyWormRepository {

    /**
     * This [getAfricanArmyWorm] and return a liveData of Insects
     * */
    override fun getAfricanArmyWorm(name : String): LiveData<List<Insect>> {
        return africanArmyWormDao.getInsectByName(name)
    }

    /**.
     * This [requestAfricanArmyWorm] is a suspend function which must be called from a coroutine
     * */
    override suspend fun requestAfricanArmyWorm() = withContext(Dispatchers.IO) {
        val response = insectApi.getAfricanArmyWorm()
        try {
            if(response.isSuccessful){
                val africanList = response.body()
                africanArmyWormDao.insertInsects(africanList)
                Timber.d("african worm $africanList")
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