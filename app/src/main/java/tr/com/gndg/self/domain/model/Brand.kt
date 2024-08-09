package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandEntity

data class Brand(
    val id: Long?,
    val uuid: String,
    val brandName: String
)

fun Brand.toBrandEntity() = BrandEntity(
    uuid = this.uuid,
    brandName = this.brandName
)