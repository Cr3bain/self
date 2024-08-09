package tr.com.gndg.self.core.dataSource.roomDatabase.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.StockEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toProduct
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toStock
import tr.com.gndg.self.domain.join.ProductStocks

data class ProductStocksEntity(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID"
    )
    val stock: List<StockEntity>,

)

fun ProductStocksEntity.toProductStocks() = ProductStocks(
    this.product.toProduct(),
    this.stock.map { it.toStock() }
)