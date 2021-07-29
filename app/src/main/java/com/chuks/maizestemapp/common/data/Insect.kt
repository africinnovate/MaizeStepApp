package com.chuks.maizestemapp.common.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chuks.maizestemapp.common.TimestampConverter
import com.chuks.maizestemapp.common.util.TimeStampConverter
import com.google.gson.annotations.SerializedName
import java.util.*
/**
 * This is the [Insect] data class
 * */
@Entity(tableName = "insect",indices = [Index(value = ["classPredictionId"], unique = true)])
data class Insect(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    @SerializedName("ImageURL_ClassName")
    var insect_image: String,
    @SerializedName("ClassName")
    var name: String,
    @SerializedName("ClassPredictionID")
    var classPredictionId: String,
    @SerializedName("Latitude")
    var latitude: String,
    @SerializedName("Longitude")
    var longitude: String,
//    @TypeConverters(TimeStampConverter::class)
    @SerializedName("PredictionDate")
    var date: String,
    @SerializedName("CaptureTime")
    var time: String,
    @SerializedName("Count_ClassName")
    var count: Int,
    @SerializedName("ET")
    var economic_threshold: Double = 2.4,
    @SerializedName("EIL")
    var economic_injury_level: Double = 3.6,
    @SerializedName("Timestamp")
    var timeStamp : Int

){
    constructor() : this(0, "", "", "", "",
        "", "", "",  0, 2.4, 3.6, 0)
}

/**
 * This is the [MaizePlot]  data class
 * */
@Entity(tableName = "maize_plot")
data class MaizePlot(
    @PrimaryKey
    val id: Int,
    val fall_armyworm: String,
    val egyptian_worm: String,
    val african_worm: String,
    val economic_injury_level: String,
    val economic_threshold: String
)


data class InsectModel(
    @SerializedName("ClassPredictionID")
    var classPredictionId: String,
    @SerializedName("ImageURL_ClassName")
    var insect_image: String,
    @SerializedName("ClassName")
    var name: String,
    @SerializedName("PredictionDate")
    var date: String,
    @SerializedName("CaptureTime")
    var time: String,
    @SerializedName("Count_ClassName")
    var count: Int
){
    constructor() : this("", "", "", "", "",
        0)
}


