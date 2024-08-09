package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SizeEntity

data class Size(
    val id : Long?,
    val uuid: String,
    val sizeName: String
)

fun Size.toSizeEntity() = SizeEntity(
    uuid = this.uuid,
    sizeName = this.sizeName,
)