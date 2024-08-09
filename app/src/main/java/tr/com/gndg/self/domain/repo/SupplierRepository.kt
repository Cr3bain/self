package tr.com.gndg.self.domain.repo

import kotlinx.coroutines.flow.MutableStateFlow
import tr.com.gndg.self.domain.model.Supplier
import tr.com.gndg.self.domain.model.SupplierUiState

interface SupplierRepository {

    val supplierUiState : MutableStateFlow<SupplierUiState>

    suspend fun getSuppliers() : List<Supplier>
    suspend fun getSupplierUUID(supplierUUID: String) : Supplier?
    suspend fun insertSupplier(supplier: Supplier) : Result<Boolean>
    suspend fun updateSupplier(supplier: Supplier) : Result<Boolean>
    suspend fun deleteSupplier(supplier: Supplier) : Result<Boolean>
}