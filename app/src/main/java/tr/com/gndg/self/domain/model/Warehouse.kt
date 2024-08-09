package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.WarehouseEntity
import java.util.UUID

data class Warehouse(
    val id: Long,
    val uuid: String,
    val name: String,
    val description: String?,
)

data class WarehouseUiState(
    val warehouse: Warehouse = WarehouseDetail().toWarehouse()
)

data class WarehouseDetail(
    var id: Long = 0,
    val uuid: String = UUID.randomUUID().toString(),
    var name: String = "",
    var description: String? = null
)

fun Warehouse.toWarehouseDetail() = WarehouseDetail(
    this.id,
    this.uuid,
    this.name,
    this.description
)


fun Warehouse.toWarehouseEntity() = WarehouseEntity (
    uuid = this.uuid,
    name = this.name,
    description = this.description
)

fun WarehouseDetail.toWarehouse() = Warehouse(
    this.id,
    this.uuid,
    this.name,
    this.description
)

fun Warehouse.toWarehouseUiState() = WarehouseUiState(
    warehouse = WarehouseDetail(
        id = this.id,
        uuid = this.uuid,
        name = this.name,
        description = this.description
    ).toWarehouse()
)