package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.StockEntity

data class Stock(
    val id: Long?,
    val uuid: String,
    val warehouseUUID: String,
    val productUUID: String,
    val warehouseID: Long,
    val productID: Long,
    val piece: Float?,
)

fun Stock.toStockEntity() = StockEntity(
    uuid = this.uuid,
    warehouseUUID = this.warehouseUUID,
    productUUID = this.productUUID,
    warehouseID = this.warehouseID,
    productID = this.productID,
    piece = this.piece,
)