package andlima.group3.secondhand.local.room.foodtable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    // Get offline product data
    @Query("SELECT * FROM FoodLocal")
    fun getOfflineProduct() : List<FoodLocal>

    // Reset data on table
    @Query("DELETE FROM FoodLocal")
    fun resetData(): Int

    // Input new data
    @Insert
    fun addData(FoodLocal: FoodLocal): Long
}