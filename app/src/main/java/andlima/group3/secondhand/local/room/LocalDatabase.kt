package andlima.group3.secondhand.local.room

import andlima.group3.secondhand.local.room.electronictable.ElectronicDao
import andlima.group3.secondhand.local.room.electronictable.ElectronicLocal
import andlima.group3.secondhand.local.room.fashiontable.FashionDao
import andlima.group3.secondhand.local.room.fashiontable.FashionLocal
import andlima.group3.secondhand.local.room.foodtable.FoodDao
import andlima.group3.secondhand.local.room.foodtable.FoodLocal
import andlima.group3.secondhand.local.room.homesupplytable.HomeSuppliesDao
import andlima.group3.secondhand.local.room.homesupplytable.HomeSuppliesLocal
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ElectronicLocal::class,
        FashionLocal::class,
        FoodLocal::class,
        HomeSuppliesLocal::class
    ],
    version = 1
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun electronicDao(): ElectronicDao
    abstract fun fashionDao(): FashionDao
    abstract fun foodDao(): FoodDao
    abstract fun homeSuppliesDao(): HomeSuppliesDao

    companion object {
        private var INSTANCE : LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase? {
            synchronized(LocalDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    LocalDatabase::class.java, "SecondHandLocal.db")
                    .build()
            }
            return INSTANCE
        }

//        fun destroyInstance() {
//            INSTANCE = null
//        }
    }
}