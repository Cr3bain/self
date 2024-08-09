package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.TaxEntity

data class Tax(
    val id : Long?,
    val uuid: String,
    val name: String,
    val percent : Float
)

fun Tax.toTaxEntity() = TaxEntity(
    uuid = this.uuid,
    name = this.name,
    percent = this.percent
)
