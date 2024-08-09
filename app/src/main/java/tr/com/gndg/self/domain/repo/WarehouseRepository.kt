package tr.com.gndg.self.domain.repo

import kotlinx.coroutines.flow.MutableStateFlow
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.WarehouseUiState

interface WarehouseRepository {

    val warehouseUiState : MutableStateFlow<WarehouseUiState>

    fun setWarehouseUiState(warehouseUiState: WarehouseUiState)

    fun getWarehouse(id: Long) : Warehouse?

    suspend fun getWarehouseUUID(uuid: String) : Warehouse?

    fun getWarehouses() : List<Warehouse>

    fun warehouseIsEmpty() : Boolean

    suspend fun updateWarehouse(warehouse: Warehouse) : Result<Boolean>

    suspend fun insertWarehouse(warehouse: Warehouse) : Result<Boolean>

    suspend fun deleteWarehouse(warehouseUUID: String): Result<Boolean>

    suspend fun deleteRequestWarehouse(warehouseUUID: String) : Result<Boolean>
}