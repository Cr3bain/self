package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductPackEntity

data class ProductPack(
    val id: Long?,
    val name: String,
    val uuid: String,
    val productUUID: String,
    val piece: Float,
    val barcode: String?,
)

fun ProductPack.toProductPackEntity() = ProductPackEntity(
    uuid = this.uuid,
    name = this.name,
    productUUID = this.productUUID,
    piece = this.piece,
    barcode = this.barcode
)
