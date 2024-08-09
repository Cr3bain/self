package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productPacks")
data class ProductPackEntity (
    val uuid: String,
    val name: String,
    val productUUID: String,
    val piece: Float,
    val barcode: String?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}