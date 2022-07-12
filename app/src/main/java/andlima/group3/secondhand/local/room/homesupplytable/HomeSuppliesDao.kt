package andlima.group3.secondhand.local.room.homesupplytable

import andlima.group3.secondhand.local.room.homesupplytable.HomeSuppliesLocal
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HomeSuppliesDao {
    // Get offline product data
    @Query("SELECT * FROM HomeSuppliesLocal")
    fun getOfflineProduct() : List<HomeSuppliesLocal>

    // Reset data on table
    @Query("DELETE FROM HomeSuppliesLocal")
    fun resetData(): Int

    // Input new data
    @Insert
    fun addData(HomeSuppliesLocal: HomeSuppliesLocal): Long
}