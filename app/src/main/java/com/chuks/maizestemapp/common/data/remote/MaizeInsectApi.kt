package com.chuks.maizestemapp.common.data.remote

import com.chuks.maizestemapp.common.data.Insect
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * This setup the Maize api and makes the necessary request
 **/
interface MaizeInsectApi {

//    @GET("5e6446133400005a003387ef")
    @GET("dev/mothPredictionAPI?ClassName=ALL")
    suspend fun getAllCapturedInsect() : Response<List<Insect>>

     @GET("dev/mothPredictionAPI/aaw?ClassName=AAW")
      fun getAfricanArmyWorm() : Response<List<Insect>>

    @GET("dev/mothPredictionAPI/eclw?ClassName=ECLW")
    suspend fun getEgyptianArmyWorm() : Response<List<Insect>>

    @GET("dev/mothPredictionAPI/faw?ClassName=FAW")
    suspend fun getFallArmyArmyWorm() : Response<List<Insect>>

    //delete an insect
    @DELETE("dev/mothPredictionAPI/delitem/")
    suspend fun deleteInsect(@Query("ClassPredictionID") ClassPredictionID: String?) : Response<Unit>

    //deleteAll Insect
    @DELETE("dev/mothPredictionAPI/delitem?DeleteAll=ALL")
    suspend fun deleteAllInsect() : Response<Unit>


}