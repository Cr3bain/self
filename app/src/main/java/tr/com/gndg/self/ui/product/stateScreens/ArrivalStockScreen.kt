package tr.com.gndg.self.ui.product.stateScreens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.joda.time.DateTime
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.ui.dialogs.ArrivalProductDialog
import tr.com.gndg.self.ui.dialogs.DatePickerDialogScreen
import tr.com.gndg.self.ui.product.presentation.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrivalStockScreen(
    toSupplierScreen: (Long) -> Unit,
) {

    val viewModel: ProductViewModel = koinViewModel()

    val datePickerState = rememberDatePickerState()

    val transactionState = viewModel.repoTransactionState.collectAsState()

    when {
        viewModel.arrivalProductDialog -> {
            ArrivalProductDialog(
                info = false,
                transactionState = transactionState.value,
                onDismissRequest = { viewModel.arrivalProductDialog = false },
                transactionDataChange = viewModel::setTransactionDataListDetail,
                transactionDetailChange=  viewModel::setTransactionDetail,
                datePickerDialog = {
                    viewModel.arrivalProductDialog = false
                    viewModel.datePickerDialog = true
                },
                toSupplierScreen = toSupplierScreen,
                saveArrival = {
                    viewModel.saveArrival()
                    viewModel.arrivalProductDialog = false
                }
                )
        }

        viewModel.datePickerDialog -> {
            DatePickerDialogScreen(datePickerState, datePickerClosed = {
                viewModel.setTransactionDetail(transactionState.value.transactionDetail.copy(date = datePickerState.selectedDateMillis?:DateTime.now().millis ))
                viewModel.datePickerDialog = false
                viewModel.arrivalProductDialog = true
            })
        }
    }
}