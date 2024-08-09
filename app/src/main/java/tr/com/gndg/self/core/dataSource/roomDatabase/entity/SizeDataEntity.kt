package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import tr.com.gndg.self.domain.model.SizeData

@Entity(tableName = "sizeData",
    primaryKeys = ["id","id"],
    indices = [Index("sizeID")],
    foreignKeys = [
        ForeignKey(
            entity = SizeEntity::class,
            parentColumns = ["id"],
            childColumns = ["sizeID"],
            /* Optional (helps maintain referential integrity) */
            /* if parent is deleted then children rows of that parent are deleted */
            onDelete = ForeignKey.CASCADE,
            /* if parent column is changed then the column that references the parent is changed to the same value */
            onUpdate = ForeignKey.CASCADE
        )
    ])
data class SizeDataEntity(
    val uuid: String,
    val sizeUUID: String,
    val productUUID: String,
    val sizeDataName: String?,
    val sizeID: Long
){
    var id : Long = 0L
}

fun SizeDataEntity.toSizeData() = SizeData(
    id = this.id,
    uuid = this.uuid,
    sizeUUID = this.sizeUUID,
    productUUID = this.productUUID,
    sizeDataName = this.sizeDataName,
    sizeID = this.sizeID
)