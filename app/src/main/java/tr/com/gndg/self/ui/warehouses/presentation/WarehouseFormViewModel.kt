package tr.com.gndg.self.ui.warehouses.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tr.com.gndg.self.domain.model.WarehouseDetail
import tr.com.gndg.self.domain.model.WarehouseUiState
import tr.com.gndg.self.domain.model.toWarehouse
import tr.com.gndg.self.domain.model.toWarehouseUiState
import tr.com.gndg.self.domain.repo.WarehouseRepository
import tr.com.gndg.self.ui.warehouses.WarehouseFormDestination

class WarehouseFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val warehouseRepository: WarehouseRepository) : ViewModel() {

    var somethingChanged by mutableStateOf(false)

    private var _uuidArg :String? by mutableStateOf(null)

    private val _repoWarehouseUiState = warehouseRepository.warehouseUiState
    val repoWarehouseUiState get() = _repoWarehouseUiState.asStateFlow()

   // var warehouseUiState : WarehouseUiState by mutableStateOf(WarehouseUiState())
   var warehouseUiState by mutableStateOf(WarehouseUiState())
        private set

    init {
        _uuidArg = savedStateHandle[WarehouseFormDestination.warehouseUUIDArgs]
    }

    fun getWarehouse(){
        if (_uuidArg != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val warehouse = warehouseRepository.getWarehouseUUID(_uuidArg!!)
                if (warehouse != null) {
                    warehouseUiState = warehouse.toWarehouseUiState()
                }
            }
        }
    }

    fun setWarehouseUiState(warehouseDetail: WarehouseDetail) {
        somethingChanged = true
        warehouseUiState = warehouseUiState.copy(warehouse = warehouseDetail.toWarehouse())
    }

    fun saveWarehouse() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_uuidArg != null) {
                //update
                warehouseRepository.updateWarehouse(warehouseUiState.warehouse)
                    .onSuccess {

                        //MAIN WAREHOUSE change
                        if (repoWarehouseUiState.value.warehouse.uuid == warehouseUiState.warehouse.uuid) {
                            warehouseRepository.setWarehouseUiState(warehouseUiState)
                        }
                }
                    .onFailure {
                    }

            } else {
                //new
                warehouseRepository.insertWarehouse(warehouseUiState.warehouse)
                    .onSuccess {

                }
                    .onFailure {

                    }
            }

            somethingChanged = false
        }

    }

}