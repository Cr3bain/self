package tr.com.gndg.self.ui.warehouses.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tr.com.gndg.self.core.preferences.sharedPreferencesLong
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.WarehouseUiState
import tr.com.gndg.self.domain.model.toWarehouseUiState
import tr.com.gndg.self.domain.repo.WarehouseRepository

class WarehousesViewModel(
    private val warehouseRepository: WarehouseRepository,
    //userRepository: UserRepository,
) : ViewModel() {

    private var firstWarehouseList= listOf<Warehouse>()
    private var isSearchStarting = true
    var searchText = mutableStateOf("")
/*
    private val _userUiState = userRepository.userUiState
    val userUiState = _userUiState.asStateFlow()*/

    private val _warehouseUiState = warehouseRepository.warehouseUiState
    val warehouseUiState get() = _warehouseUiState.asStateFlow()

    private var _warehouseList = MutableStateFlow<List<Warehouse>>(listOf())
    val warehouseList get() = _warehouseList.asStateFlow()

    fun setPrimeWarehouseUiState(warehouseUiState: WarehouseUiState) = warehouseRepository.setWarehouseUiState(warehouseUiState)

    fun getWarehouses()  {
        viewModelScope.launch(Dispatchers.IO) {
            _warehouseList.value = warehouseRepository.getWarehouses()
        }

    }

    suspend fun deleteWarehouseRequest(warehouse: Warehouse) : Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext warehouseRepository.deleteRequestWarehouse(warehouse.uuid)
    }

    fun deleteWarehouse(warehouse: Warehouse, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            warehouseRepository.deleteWarehouse(warehouse.uuid)
                .onSuccess {

                    //Deleted warehouse if prime warehouse, prime will be set to main warehouse
                    if (warehouse.uuid == warehouseUiState.value.warehouse.uuid) {
                        warehouseRepository.getWarehouse(1L)?.let {
                            setPrimeWarehouseUiState(it.toWarehouseUiState())
                        }
                        var warehousePreferences by context.sharedPreferencesLong("warehouse")
                        warehousePreferences = 1L
                    }
                    getWarehouses()
                }
                .onFailure {
                    Log.e("WarehousesViewModel", it.message.toString())
                }
        }

    }

    fun searchWarehouse(query : String){

        val listToSearch= if(isSearchStarting){
            _warehouseList.value
        } else{
            firstWarehouseList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                _warehouseList.value=firstWarehouseList
                isSearchStarting=true
                return@launch
            }
            val results=listToSearch.filter {
                it.name.contains(query.trim(),true)
            }
            if(isSearchStarting){
                firstWarehouseList=_warehouseList.value
                isSearchStarting=false

            }
            _warehouseList.value= results
        }

    }

}