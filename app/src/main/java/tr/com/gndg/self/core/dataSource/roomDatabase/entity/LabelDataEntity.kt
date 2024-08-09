package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import tr.com.gndg.self.domain.model.LabelData

@Entity(tableName = "labelData",
    primaryKeys = ["id","id"],
    indices = [Index(value = arrayOf("id"), unique = true), Index("labelID")],
    foreignKeys = [
        ForeignKey(
            entity = LabelEntity::class,
            parentColumns = ["id"],
            childColumns = ["labelID"],
            /* Optional (helps maintain referential integrity) */
            /* if parent is deleted then children rows of that parent are deleted */
            onDelete = ForeignKey.CASCADE,
            /* if parent column is changed then the column that references the parent is changed to the same value */
            onUpdate = ForeignKey.CASCADE
        )
    ])
data class LabelDataEntity(
    val uuid: String,
    val labelUUID: String,
    val productUUID: String,
    val labelDataName: String?,
    val labelID: Long
) {
    var id : Long = 0L
}

fun LabelDataEntity.toLabelData() = LabelData(
    id = this.id,
    uuid = this.uuid,
    labelUUID = this.labelUUID,
    productUUID = this.productUUID,
    labelDataName = this.labelDataName,
    labelID = this.labelID
)