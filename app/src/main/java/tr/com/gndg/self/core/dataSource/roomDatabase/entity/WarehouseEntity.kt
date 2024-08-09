package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.Warehouse

@Entity(tableName = "warehouses")
data class WarehouseEntity(
    val uuid: String,
    val name: String,
    val description: String?,

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

fun WarehouseEntity.toWarehouse() = Warehouse (id, uuid, name, description)
