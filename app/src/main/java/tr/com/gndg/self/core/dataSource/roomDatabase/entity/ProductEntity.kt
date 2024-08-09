package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.ProductDetails
import java.math.BigDecimal

@Entity(tableName = "products")
data class ProductEntity(
    val uuid: String,
    val name: String,
    val description: String?,
    val barcode: String?,
    val stockCode: String?,
    val minStock: BigDecimal?,
    val avrPurchasePrice: BigDecimal?,
    val purchasePrice: BigDecimal?,
    val sellPrice: BigDecimal?

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

fun ProductEntity.toProduct() = Product (
    id = this.id,
    uuid = this.uuid,
    name = this.name,
    description = this.description,
    barcode = this.barcode,
    stockCode = this.stockCode,
    minStock = this.minStock,
    avrPurchasePrice = this.avrPurchasePrice,
    purchasePrice = this.purchasePrice,
    sellPrice = this.sellPrice
)

fun ProductEntity.toProductDetail() = ProductDetails (
    id = 0,
    uuid = this.uuid,
    name = this.name,
    description = description,
    barcode = barcode,
    stockCode = stockCode,
    minStock = minStock,
    avrPurchasePrice = avrPurchasePrice,
    purchasePrice = purchasePrice,
    sellPrice = sellPrice
)