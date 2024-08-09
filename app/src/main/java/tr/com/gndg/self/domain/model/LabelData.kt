package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.LabelDataEntity

data class LabelData(
    val id: Long,
    val uuid: String,
    val labelUUID: String,
    val productUUID: String,
    val labelDataName: String?,
    val labelID: Long
)

fun LabelData.toLabelDataEntity() = LabelDataEntity(
    uuid = this.uuid,
    labelUUID = this.labelUUID,
    productUUID = this.productUUID,
    labelDataName = this.labelDataName,
    labelID = this.labelID
)
