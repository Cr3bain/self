package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.LabelEntity

data class Label(
    val id : Long?,
    val uuid: String,
    val labelName: String
)

fun Label.toLabelEntity() = LabelEntity(
    uuid = this.uuid, labelName = this.labelName
)