package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SizeDataEntity

data class SizeData(
    val id: Long,
    val uuid: String,
    val sizeUUID: String,
    val productUUID: String,
    val sizeDataName: String?,
    val sizeID: Long
)

fun SizeData.toSizeDataEntity() = SizeDataEntity(
    uuid = this.uuid,
    sizeUUID = this.sizeUUID,
    productUUID = this.productUUID,
    sizeDataName = this.sizeDataName,
    sizeID = this.sizeID
)