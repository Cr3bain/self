package tr.com.gndg.self.ui.scopes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.domain.repo.CustomerRepository
import tr.com.gndg.self.domain.repo.SupplierRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository

@Composable
fun SourceAndTargetNameText(
    transactionType: Int,
    sourceTargetUUID: String?
){
    val supplierRepository : SupplierRepository = koinInject()
    val customerRepository : CustomerRepository = koinInject()
    val warehouseRepository : WarehouseRepository = koinInject()

    var result by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = sourceTargetUUID) {
        this.launch(Dispatchers.IO) {

            if (sourceTargetUUID != null) {
                when(transactionType) {
                    TransactionType.Arrival -> {
                        //supplier
                        val supplier = supplierRepository.getSupplierUUID(sourceTargetUUID)
                        if (supplier != null) {
                            result = supplier.name
                        }

                    }
                    TransactionType.Outgoing -> {
                        //customer
                        val customer = customerRepository.getCustomerByUUID(sourceTargetUUID)
                        if (customer != null) {
                            result = customer.name
                        }

                    }
                    TransactionType.Transfer -> {
                        //other warehouse
                        val warehouse = warehouseRepository.getWarehouseUUID(sourceTargetUUID)
                        if(warehouse != null) {
                            result = warehouse.name
                        }

                    }
                }

            }
        }
    }


    Text(text = result)
}