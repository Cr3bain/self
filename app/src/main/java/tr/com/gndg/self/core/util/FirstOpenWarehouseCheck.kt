package tr.com.gndg.self.core.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tr.com.gndg.self.R
import tr.com.gndg.self.core.preferences.sharedPreferencesLong
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.toWarehouseUiState
import tr.com.gndg.self.domain.repo.WarehouseRepository
import java.util.UUID

fun warehouseCheck(warehouseRepository: WarehouseRepository, context: Context) {

    CoroutineScope(Dispatchers.IO).launch {
        var warehousePreferences by context.sharedPreferencesLong("warehouse")
        if (warehousePreferences == -1L) {

            if (warehouseRepository.warehouseIsEmpty()) {
                val uuid = UUID.randomUUID().toString()

                warehouseRepository.insertWarehouse(
                    Warehouse(
                        id = 1L,
                        uuid = uuid,
                        name = context.getString(R.string.mainWarehouse),
                        description = null
                    )
                ).onSuccess {
                    //First warehouse
                    warehousePreferences = 1L
                    warehouseRepository.getWarehouseUUID(uuid)?.let {warehouse->
                        warehouseRepository.setWarehouseUiState(
                            warehouse.toWarehouseUiState()
                        )
                    }
                }.onFailure {
                    Log.e("MainApp", it.message.toString())

                    warehousePreferences = 0L
                    warehouseRepository.setWarehouseUiState(
                        warehouseZero(context).toWarehouseUiState()
                    )
                }
            }

        } else {
            if (warehousePreferences == 0L) {
                warehouseRepository.setWarehouseUiState(
                    warehouseZero(context).toWarehouseUiState()
                )
            } else {
                warehouseRepository.getWarehouse(warehousePreferences)?.let { warehouse->
                    warehouseRepository.setWarehouseUiState(
                        warehouse.toWarehouseUiState()
                    )
                    warehouseRepository.warehouseUiState.value = warehouse.toWarehouseUiState()
                }
            }

        }

    }
}