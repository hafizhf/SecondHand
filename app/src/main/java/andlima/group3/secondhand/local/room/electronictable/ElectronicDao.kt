package andlima.group3.secondhand.local.room.electronictable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ElectronicDao {
    // Get offline product data
    @Query("SELECT * FROM ElectronicLocal")
    fun getOfflineProduct() : List<ElectronicLocal>

    // Reset data on table
    @Query("DELETE FROM ElectronicLocal")
    fun resetData(): Int

    // Input new data
    @Insert
    fun addData(electronicLocal: ElectronicLocal): Long
}