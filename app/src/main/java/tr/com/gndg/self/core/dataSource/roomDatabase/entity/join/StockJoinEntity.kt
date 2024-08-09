package tr.com.gndg.self.core.dataSource.roomDatabase.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.StockEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.WarehouseEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toProduct
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toStock
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toWarehouse
import tr.com.gndg.self.domain.join.StockJoin

data class StockJoinEntity(
    @Embedded
    val stock: StockEntity,
    @Relation(
        parentColumn = "productUUID",
        entityColumn = "uuid")
    val product: ProductEntity?,
    @Relation(
        parentColumn = "warehouseUUID",
        entityColumn = "uuid")
    val warehouse: WarehouseEntity?,
)

fun StockJoinEntity.toStockJoin() = StockJoin(
    this.stock.toStock(),
    this.product?.toProduct(),
    this.warehouse?.toWarehouse()
)