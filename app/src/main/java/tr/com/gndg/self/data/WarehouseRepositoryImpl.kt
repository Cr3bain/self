package tr.com.gndg.self.data

import kotlinx.coroutines.flow.MutableStateFlow
import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.WarehouseUiState
import tr.com.gndg.self.domain.repo.WarehouseRepository

class WarehouseRepositoryImpl(private val dataSourceRepository: DataSourceRepository) : WarehouseRepository {


    private var _warehouseUiState = MutableStateFlow(WarehouseUiState())

    override val warehouseUiState: MutableStateFlow<WarehouseUiState> get() = _warehouseUiState

    override fun setWarehouseUiState(warehouseUiState: WarehouseUiState) {
        _warehouseUiState.value = warehouseUiState
    }

    override fun getWarehouse(id: Long): Warehouse? {
        return dataSourceRepository.getWarehouse(id)
    }

    override suspend fun getWarehouseUUID(uuid: String): Warehouse? {
        return dataSourceRepository.getWarehouseUUID(uuid)
    }

    override fun getWarehouses(): List<Warehouse> {
        return dataSourceRepository.getWarehouses()
    }

    override fun warehouseIsEmpty(): Boolean {
        return dataSourceRepository.warehouseIsEmpty()
    }


    override suspend fun updateWarehouse(warehouse: Warehouse): Result<Boolean> {
        return dataSourceRepository.updateWarehouse(warehouse)
    }

    override suspend fun insertWarehouse(warehouse: Warehouse): Result<Boolean> {
        return dataSourceRepository.insertWarehouse(warehouse)
    }

    override suspend fun deleteWarehouse(warehouseUUID: String) : Result<Boolean> {
        return dataSourceRepository.deleteWarehouse(warehouseUUID)
    }

    override suspend fun deleteRequestWarehouse(warehouseUUID: String): Result<Boolean> {
        return dataSourceRepository.deleteRequestWarehouse(warehouseUUID)
    }

}