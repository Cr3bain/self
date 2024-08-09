package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.Stock

@Entity(tableName = "stocks",
    indices = [Index(value = arrayOf("id"), unique = true), Index("warehouseID"), Index("productID")],
    foreignKeys = [
        /* A MovieId MUST be a value of an existing id column in the movie table */
        ForeignKey(
            entity = WarehouseEntity::class,
            parentColumns = ["id"],
            childColumns = ["warehouseID"],
            /* Optional (helps maintain referential integrity) */
            /* if parent is deleted then children rows of that parent are deleted */
            onDelete = ForeignKey.CASCADE,
            /* if parent column is changed then the column that references the parent is changed to the same value */
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productID"],
            /* Optional (helps maintain referential integrity) */
            /* if parent is deleted then children rows of that parent are deleted */
            onDelete = ForeignKey.CASCADE,
            /* if parent column is changed then the column that references the parent is changed to the same value */
            onUpdate = ForeignKey.CASCADE
        )
    ])
data class StockEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val uuid: String,
    val warehouseUUID: String,
    val productUUID: String,
    val warehouseID: Long,
    val productID: Long,
    val piece: Float?,
)
fun StockEntity.toStock() = Stock (
    id = this.id,
    uuid = this.uuid,
    warehouseUUID = this.warehouseUUID,
    productUUID = this.productUUID,
    warehouseID = this.warehouseID,
    productID = this.productID,
    piece = this.piece
)