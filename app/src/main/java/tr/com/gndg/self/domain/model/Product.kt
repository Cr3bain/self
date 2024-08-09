package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductEntity
import java.math.BigDecimal
import java.util.UUID


data class Product(
    val id: Long,
    val uuid: String,
    val name: String,
    val description: String?,
    val barcode: String?,
    val stockCode: String?,
    val minStock: BigDecimal?,
    val avrPurchasePrice: BigDecimal?,
    val purchasePrice: BigDecimal?,
    val sellPrice: BigDecimal?
)

data class ProductUiState(
    val productDetails: ProductDetails = ProductDetails(),
    val isEntryValid: Boolean = false
)

data class ProductDetails(
    val id: Long = 0,
    val uuid: String = "",
    val name: String = "",
    val description: String? = null,
    val barcode: String? = null,
    val stockCode: String? = null,
    val minStock: BigDecimal? = null,
    val avrPurchasePrice: BigDecimal? = null,
    val purchasePrice: BigDecimal? = null,
    val sellPrice: BigDecimal? = null,
)

fun Product.toProductEntity() = ProductEntity (
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
fun Product.toProductUiState() = ProductUiState(
    productDetails = ProductDetails(
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
    ),
    isEntryValid = true
)

fun ProductDetails.toProduct() = Product(
    id = this.id,
    uuid = this.uuid.ifEmpty { UUID.randomUUID().toString() },
    name = this.name,
    description = this.description,
    barcode = this.barcode,
    stockCode = this.stockCode,
    minStock = this.minStock,
    avrPurchasePrice = this.avrPurchasePrice,
    purchasePrice = this.purchasePrice,
    sellPrice = this.sellPrice
)

fun Product.toProductDetails() = ProductDetails(
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