package tr.com.gndg.self.data

import kotlinx.coroutines.flow.MutableStateFlow
import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.domain.model.Supplier
import tr.com.gndg.self.domain.model.SupplierUiState
import tr.com.gndg.self.domain.repo.SupplierRepository

class SupplierRepositoryImpl(private val dataSourceRepository: DataSourceRepository) : SupplierRepository {

    private var _supplierUiState = MutableStateFlow(SupplierUiState())

    override val supplierUiState: MutableStateFlow<SupplierUiState>
        get() = _supplierUiState

    override suspend fun getSuppliers(): List<Supplier> = dataSourceRepository.getSuppliers()

    override suspend fun getSupplierUUID(supplierUUID: String): Supplier? = dataSourceRepository.getSupplierUUID(supplierUUID)

    override suspend fun insertSupplier(supplier: Supplier): Result<Boolean> {
        return dataSourceRepository.insertSupplier(supplier)
    }
    override suspend fun updateSupplier(supplier: Supplier): Result<Boolean> {
        return dataSourceRepository.updateSupplier(supplier)
    }

    override suspend fun deleteSupplier(supplier: Supplier): Result<Boolean> {
        return dataSourceRepository.deleteSupplier(supplier)
    }

}