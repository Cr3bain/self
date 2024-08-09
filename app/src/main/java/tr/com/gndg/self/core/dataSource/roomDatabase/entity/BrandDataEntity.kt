package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.BrandData

@Entity(tableName = "brandData" ,
    indices = [Index(value = arrayOf("id"), unique = true), Index("brandID")],
    foreignKeys = [
        /* A MovieId MUST be a value of an existing id column in the movie table */
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = ["id"],
            childColumns = ["brandID"],
            /* Optional (helps maintain referential integrity) */
            /* if parent is deleted then children rows of that parent are deleted */
            onDelete = ForeignKey.CASCADE,
            /* if parent column is changed then the column that references the parent is changed to the same value */
            onUpdate = ForeignKey.CASCADE
        )
    ])
data class BrandDataEntity(
    val uuid: String,
    val brandUUID: String,
    val productUUID: String,
    val brandDataName: String?,
    val brandID: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L
}

fun BrandDataEntity.toBrandData() = BrandData(
    id = this.id,
    uuid = this.uuid,
    brandUUID = this.brandUUID,
    productUUID = this.productUUID,
    brandDataName = this.brandDataName,
    brandID = this.brandID
)