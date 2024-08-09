package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.WarehouseEntity

@Dao
interface WarehouseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(warehouse: WarehouseEntity)

    @Query("UPDATE warehouses SET name=:name, description=:description WHERE uuid=:warehouseUUID")
    suspend fun update(name: String, description: String?, warehouseUUID: String)

    @Query("DELETE from warehouses WHERE uuid = :warehouseUUID")
    suspend fun deleteWarehouse(warehouseUUID: String)

    @Query("SELECT * from warehouses WHERE id = :id")
    fun getWarehouse(id: Long): WarehouseEntity?

    @Query("SELECT * from warehouses WHERE uuid = :uuid")
    fun getWarehouseUUID(uuid: String): WarehouseEntity?

    @Query("SELECT * from warehouses ORDER BY id ASC")
    fun getWarehouses(): List<WarehouseEntity>

    @Query("DELETE from warehouses")
    fun deleteALLWarehouses()

    @Query("SELECT (SELECT COUNT(*) FROM warehouses) == 0")
    fun isEmpty(): Boolean
}