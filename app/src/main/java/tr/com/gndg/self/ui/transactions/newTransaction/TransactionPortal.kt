package tr.com.gndg.self.ui.transactions.newTransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.transactions.newTransaction.presentation.NewTransactionViewModel


object TransactionPortalDestination : NavigationDestination {
    override val route = "transaction_portal"
    override val titleRes = R.string.portal
    const val transactionTypeArgs = "transactionType"
    val routeWithArgs = "${route}/{$transactionTypeArgs}"
}
@Composable
fun TransactionPortal(
    navigateBack: () -> Unit,
    toTransactionWarehouseSelectScreen: (Int) -> Unit,
    toNewTransactionScreen : () -> Unit,
    viewModel: NewTransactionViewModel = koinViewModel()
){

    val warehouseUiState = viewModel.warehouseUiState
    val transactionState = viewModel.repositoryTransactionState

    LaunchedEffect(key1 = viewModel.transactionTypeArg){
        when(viewModel.transactionTypeArg) {
            -1 -> {
                navigateBack()
                //viewModel.resetTransactionState()
            }
            TransactionType.Arrival -> {
                if (warehouseUiState.value.warehouse.id == 0L && transactionState.value.transactionDetail.targetID == null) {
                    //ArrivalState için bir target belirlememiz gerek
                    toTransactionWarehouseSelectScreen(TransactionType.Arrival)

                } else {
                    if (transactionState.value.transactionDetail.targetID == null &&  transactionState.value.transactionDetail.targetUUID == null) {
                        val set = transactionState.value.transactionDetail.copy(
                            transactionType = TransactionType.Arrival,
                            documentNumber = viewModel.getLastTransactionID(),
                            targetUUID = warehouseUiState.value.warehouse.uuid,
                            targetID = warehouseUiState.value.warehouse.id
                        )
                        viewModel.setTransactionDetail(set)
                        toNewTransactionScreen()
                    } else {
                        val setType = transactionState.value.transactionDetail.copy(
                            transactionType = TransactionType.Arrival,
                            documentNumber = viewModel.getLastTransactionID()
                        )
                        viewModel.setTransactionDetail(setType)
                        toNewTransactionScreen()
                    }


                }

            }
            TransactionType.Outgoing -> {
                if (warehouseUiState.value.warehouse.id == 0L && transactionState.value.transactionDetail.sourceID == null) {
                    //Outgoing için bir source belirlememiz gerek
                    toTransactionWarehouseSelectScreen(TransactionType.Outgoing)

                } else {
                    if (transactionState.value.transactionDetail.sourceID == null &&  transactionState.value.transactionDetail.sourceUUID == null) {
                        val set = transactionState.value.transactionDetail.copy(
                            transactionType = TransactionType.Outgoing,
                            documentNumber = viewModel.getLastTransactionID(),
                            sourceUUID = warehouseUiState.value.warehouse.uuid,
                            sourceID = warehouseUiState.value.warehouse.id
                        )
                        viewModel.setTransactionDetail(set)
                        toNewTransactionScreen()
                    } else {
                        val setType = transactionState.value.transactionDetail.copy(
                            transactionType = TransactionType.Outgoing,
                            documentNumber = viewModel.getLastTransactionID()
                        )
                        viewModel.setTransactionDetail(setType)
                        toNewTransactionScreen()
                    }


                }

            }
            TransactionType.Transfer -> {
                if (transactionState.value.transactionDetail.sourceID == null) {
                    toTransactionWarehouseSelectScreen(TransactionType.Transfer)

                } else if(transactionState.value.transactionDetail.targetID == null) {
                    toTransactionWarehouseSelectScreen(TransactionType.TransferTarget)

                } else {
                    val set = transactionState.value.transactionDetail.copy(
                        transactionType = TransactionType.Transfer,
                        documentNumber = viewModel.getLastTransactionID()
                    )
                    viewModel.setTransactionDetail(set)
                    toNewTransactionScreen()

                }

            }
            //transaction Warehouse select screen target ta seçtiğimizden 4 döndürüyor.
            TransactionType.TransferTarget -> {
                val set = transactionState.value.transactionDetail.copy(
                    transactionType = TransactionType.Transfer,
                    documentNumber = viewModel.getLastTransactionID()
                )
                viewModel.setTransactionDetail(set)
                toNewTransactionScreen()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {



    }
}

