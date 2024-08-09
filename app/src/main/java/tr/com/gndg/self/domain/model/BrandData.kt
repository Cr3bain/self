package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandDataEntity

data class BrandData(
    val id: Long,
    val uuid: String,
    val brandUUID: String,
    val productUUID: String,
    val brandDataName: String?,
    val brandID: Long
)

fun BrandData.toBrandDataEntity() = BrandDataEntity(
    uuid = this.uuid,
    brandUUID = this.brandUUID,
    productUUID = this.productUUID,
    brandDataName = this.brandDataName,
    brandID = this.brandID
)
