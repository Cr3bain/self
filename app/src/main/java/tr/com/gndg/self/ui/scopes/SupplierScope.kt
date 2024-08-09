package tr.com.gndg.self.ui.scopes

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.domain.model.transactions.TransactionState
import tr.com.gndg.self.domain.repo.CustomerRepository
import tr.com.gndg.self.domain.repo.SupplierRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository

@Composable
fun SourceTargetTextField(
    modifier: Modifier,
    transactionState: TransactionState,
    sourceTargetSelect : (Long) -> Unit,
    buttonEnable : Boolean = true
) {
    val sourceRepo : SupplierRepository = koinInject()
    val customerRepo : CustomerRepository = koinInject()
    val warehouseRepo : WarehouseRepository = koinInject()

    val sourceTargetName = remember {
        mutableStateOf("")
    }

    val label = remember {
        mutableStateOf("")
    }

    val td = transactionState.transactionDetail

    @Composable
    fun IconButton() {
        when(td.transactionType) {
            TransactionType.Arrival -> {
                label.value = stringResource(id = R.string.supplier)

                IconButton(
                    enabled = buttonEnable,
                    onClick = {
                        sourceTargetSelect(td.transactionID)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.supplier_48),
                        contentDescription = "supplier",
                        modifier = Modifier.size(48.dp)
                    )

                }
            }

            TransactionType.Outgoing -> {
                label.value = stringResource(id = R.string.customer)
                IconButton(
                    enabled = buttonEnable,
                    onClick = {
                        sourceTargetSelect(td.transactionID)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.customer_48),
                        contentDescription = "customer",
                        modifier = Modifier.size(48.dp)
                    )

                }
            }

            TransactionType.Transfer -> {
                IconButton(
                    enabled = false,
                    onClick = {

                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.move_stock_48),
                        contentDescription = "customer",
                        modifier = Modifier.size(48.dp)
                    )

                }
            }

            else -> {
                IconButton(
                    enabled = false,
                    onClick = {

                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.supplier_48),
                        contentDescription = "supplier",
                        modifier = Modifier.size(48.dp)
                    )

                }
            }
        }
    }

    LaunchedEffect(key1 = transactionState) {

        this.launch(Dispatchers.IO) {
            when(td.transactionType) {

                TransactionType.Arrival -> {

                    val uuid = td.sourceUUID
                    if (uuid != null) {
                        sourceRepo.getSupplierUUID(uuid)?.let {
                            sourceTargetName.value = it.name
                        }
                    }

                }
                TransactionType.Outgoing -> {
                    val uuid = td.targetUUID
                    if (uuid != null) {
                        customerRepo.getCustomerByUUID(uuid)?.let {
                            sourceTargetName.value = it.name
                        }
                    }

                }
                TransactionType.Transfer -> {
                    val uuid = td.targetUUID
                    if (uuid != null) {
                        warehouseRepo.getWarehouseUUID(uuid)?.let {
                            sourceTargetName.value = it.name
                        }
                    }

                }
            }
        }

    }

    OutlinedTextField(
        modifier = modifier
        ,
        readOnly = true,
        value = sourceTargetName.value,
        onValueChange = {

        },
        label =  { Text(label.value) },
        singleLine = true,
        isError = false,
        trailingIcon = {

            IconButton()
        }
    )
}
