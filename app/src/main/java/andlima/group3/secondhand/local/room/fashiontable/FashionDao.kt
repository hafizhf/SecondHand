package andlima.group3.secondhand.local.room.fashiontable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FashionDao {
    // Get offline product data
    @Query("SELECT * FROM FashionLocal")
    fun getOfflineProduct() : List<FashionLocal>

    // Reset data on table
    @Query("DELETE FROM FashionLocal")
    fun resetData(): Int

    // Input new data
    @Insert
    fun addData(FashionLocal: FashionLocal): Long
}