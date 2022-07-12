package andlima.group3.secondhand.local.room.homesupplytable

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class HomeSuppliesLocal(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "basePrice")
    val basePrice: Int?,

    @ColumnInfo(name = "location")
    val location: String?,

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray?
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeSuppliesLocal

        if (id != other.id) return false
        if (name != other.name) return false
        if (basePrice != other.basePrice) return false
        if (location != other.location) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (basePrice ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}