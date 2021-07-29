package com.chuks.maizestemapp.common.data.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chuks.maizestemapp.capturedinsect.dao.InsectDao
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.dao.AfricanArmyWormDao
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.dao.EgyptianArmyWormDao
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.dao.FallArmyWormDao
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.data.MaizePlot
import com.chuks.maizestemapp.maizestemplot.dao.MaizePlotDao

/**
 * This setup the Room Database to enable
 * offline storage of data
 **/
@Database(entities = [Insect::class, MaizePlot::class], version = 1, exportSchema = false)
abstract class InsectDatabase : RoomDatabase() {
    abstract val capturedInsectDao: InsectDao
    abstract val africanArmyWormDao: AfricanArmyWormDao
    abstract val egyptianArmyWormDao: EgyptianArmyWormDao
    abstract val fallArmyWormDao: FallArmyWormDao
    abstract val maizePlot: MaizePlotDao
}