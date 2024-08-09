package tr.com.gndg.self.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.core.util.bigDecimalToString
import tr.com.gndg.self.core.util.stringToBigDecimal
import tr.com.gndg.self.core.util.stringToFloat
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.ui.inventory.presentation.InventoryViewModel
import tr.com.gndg.self.ui.texts.ProductStockText

@Composable
fun TransactionProductDialog(
    onDismissRequest : () -> Unit,
    productsJoin: ProductsJoin,
    saved: () -> Unit,
    failed : (Throwable) -> Unit
){
    val transactionRepository : TransactionRepository = koinInject()
    val inventoryViewModel: InventoryViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()

    val transactionState = transactionRepository.transactionState.value
    val transactionType = transactionState.transactionDetail.transactionType

    var wannaPiece by remember {
        mutableStateOf("")
    }

    var unitPrice by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = transactionType) {
        when(transactionType) {
            TransactionType.Arrival -> {
                unitPrice = bigDecimalToString(productsJoin.product.purchasePrice)
            }
            TransactionType.Outgoing -> {
                unitPrice = bigDecimalToString(productsJoin.product.sellPrice)
            }
            TransactionType.Transfer -> {
                unitPrice = "0"
            }
            TransactionType.TransferTarget -> {
                unitPrice = "0"
            }
        }
    }

    val warehouseUUID = transactionRepository.transactionWarehouseUUIDByTypes()


    var stockPiece = 0f

    fun adequateStockCheck() : Boolean {
        return when {
            wannaPiece.isBlank() -> {
                false
            }
            stringToFloat(wannaPiece) == null -> {
                false
            }
            transactionType == TransactionType.Outgoing && (stringToFloat(wannaPiece)
                ?: 0F) > stockPiece -> {
                false
            }
            transactionType == TransactionType.Transfer && (stringToFloat(wannaPiece)
                ?: 0F) > stockPiece -> {
                false
            } else -> {
                true
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {

        Card(modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_extra_small))
            .fillMaxWidth()
            //.height(400.dp)
            ,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)),) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {

                Text(text = stringResource(id = R.string.stockIn))
                ProductStockText(warehouseUUID = warehouseUUID, stockList = productsJoin.stock ).let {
                    it?.let {
                        stockPiece = it
                    }
                }

                OutlinedTextField(
                    value = wannaPiece,
                    onValueChange = { wannaPiece = it },
                    label = {
                    Text(text = stringResource(id = R.string.piece))
                },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = !adequateStockCheck()
                )

                if (transactionType != TransactionType.Transfer) {
                    OutlinedTextField(
                        value = unitPrice,
                        onValueChange = { unitPrice = it },
                        label = {
                            Text(text = stringResource(id = R.string.perUnitPrice))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = unitPrice.isNotBlank() && stringToBigDecimal(unitPrice) == null,
                    )
                }



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton (onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }


                    TextButton(onClick = {

                        coroutineScope.launch {
                            inventoryViewModel.addProductToTransactionStateList(productsJoin, wannaPiece, unitPrice)
                                .onSuccess {
                                    saved()
                                }.onFailure {
                                    failed(it)
                                }
                        }


                    },
                        enabled = adequateStockCheck() && unitPrice.isBlank() || adequateStockCheck() && stringToBigDecimal(unitPrice) != null
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }

    }

}