package tr.com.gndg.self.ui.transactions.newTransaction

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.transactions.newTransaction.presentation.NewTransactionViewModel
import tr.com.gndg.self.ui.warehouses.WarehouseSelectBody

object TransactionWarehouseSelectScreenDestination : NavigationDestination {
    override val route = "transactionWarehouseSelect"
    override val titleRes = R.string.warehouseSelectScreen_title
    const val transactionTypeForWarehouseArgs = "transactionTypeForWarehouse"
    val routeWithArgs = "${route}/{$transactionTypeForWarehouseArgs}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionWarehouseSelectScreen(
    selectDoneNavigateBack: (Int) -> Unit,
    viewModel: NewTransactionViewModel = koinViewModel()
){

    viewModel.getWarehouses()

    val warehouseList = viewModel.warehouseList.collectAsState()

    val transactionState = viewModel.repositoryTransactionState

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val transactionType = viewModel.transactionTypeForWarehouseArgs

    val titleString = when(transactionType) {
        TransactionType.Transfer -> {
            stringResource(R.string.warehouseTransferSource_title)
        }
        TransactionType.TransferTarget -> {
            stringResource(R.string.warehouseTransferTarget_title)
        } else -> {
            stringResource(R.string.warehouseSelectScreen_title)
        }
    }

    val filteredWarehouse = when(transactionType) {
        TransactionType.TransferTarget -> {
            warehouseList.value.filter { it.id != transactionState.value.transactionDetail.sourceID }
        } else -> {
            warehouseList.value
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SelfTopAppBar(
                title = titleString,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = { },
                navigateBack = {
                    selectDoneNavigateBack(-1)
                },
                action = {

                }
            )
        },
    ) {innerPadding->

        WarehouseSelectBody(
            modifier = Modifier.padding(innerPadding),
            warehouseList = filteredWarehouse,
            selectedWarehouse = 0L,
            setWarehouseState = {
                val set =
                    when(transactionType) {
                        TransactionType.Arrival -> {
                            transactionState.value.transactionDetail.copy(
                                transactionType = transactionType,
                                targetUUID = it.uuid,
                                targetID = it.id
                            )

                        }
                        TransactionType.Outgoing -> {
                            transactionState.value.transactionDetail.copy(
                                transactionType = transactionType,
                                sourceUUID = it.uuid,
                                sourceID = it.id
                            )
                        }
                        TransactionType.Transfer -> {
                            transactionState.value.transactionDetail.copy(
                                transactionType = transactionType,
                                sourceUUID = it.uuid,
                                sourceID = it.id
                            )

                        } else -> {
                        //Transfer Target
                        transactionState.value.transactionDetail.copy(
                            transactionType = TransactionType.Transfer,
                            targetUUID = it.uuid,
                            targetID = it.id
                        )
                        }


                    }
                viewModel.setTransactionDetail(set)
                selectDoneNavigateBack(transactionType?:0)
            },
        )
    }
}