package tr.com.gndg.self.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import org.joda.time.DateTime
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.stringToBigDecimal
import tr.com.gndg.self.core.util.stringToFloat
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.model.transactions.TransactionState
import tr.com.gndg.self.ui.scopes.SourceTargetTextField
import tr.com.gndg.self.ui.scopes.WarehouseNameText
import tr.com.gndg.self.ui.textFields.DatePickerRow

@Preview
@Composable
fun StockArrivalPreview() {
  ArrivalProductDialog(
      info= false,
      transactionState = TransactionState(),
      onDismissRequest = { /*TODO*/ },
      transactionDataChange = {},
      transactionDetailChange = {},
      datePickerDialog = { /*TODO*/ },
      toSupplierScreen = { },
      saveArrival = {} )
}

@Composable
fun ArrivalProductDialog(
    info: Boolean,
    transactionState: TransactionState,
    onDismissRequest : () -> Unit,
    transactionDataChange: (TransactionDataDetail) -> Unit,
    transactionDetailChange: (TransactionDetail) -> Unit,
    datePickerDialog: () -> Unit,
    toSupplierScreen:(Long) -> Unit,
    saveArrival: () -> Unit,
) {

    val selectedDate by remember {
        mutableStateOf(DateTime(transactionState.transactionDetail.date))
    }


    val transactionData = transactionState.dataList.first()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_extra_small))
                .fillMaxWidth()
                //.height(400.dp)
                ,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)),
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {

                WarehouseNameText(
                    modifier = Modifier,
                    warehouseUUID = transactionState.transactionDetail.targetUUID,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    readOnly = info,
                    modifier = Modifier.fillMaxWidth(),
                    value = transactionData.piece,
                    onValueChange = {  transactionDataChange (
                        transactionData.copy(piece = it) ) },
                    label = { Text(stringResource(id = R.string.piece)) },
                    singleLine = true,
                    isError = stringToFloat(transactionData.piece) == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                //Date Row
                DatePickerRow(selectedDate = selectedDate, info = info) {
                    datePickerDialog()
                }


                //Suppier Row

                SourceTargetTextField(
                    modifier = Modifier.fillMaxWidth(),
                    transactionState = transactionState,
                    sourceTargetSelect = toSupplierScreen,
                    buttonEnable = !info)

                OutlinedTextField(
                    readOnly = info,
                    modifier = Modifier.fillMaxWidth(),
                    value = transactionData.unitPrice,
                    onValueChange = {
                        transactionDataChange(transactionData.copy(unitPrice = it)) },
                    label = { Text(stringResource(id = R.string.unitPrice)) },
                    singleLine = true,
                    isError = transactionData.unitPrice.isNotBlank() && stringToBigDecimal(transactionData.unitPrice) == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    readOnly = info,
                    modifier = Modifier.fillMaxWidth(),
                    value = transactionState.transactionDetail.description?:"",
                    onValueChange = {
                        transactionDetailChange(transactionState.transactionDetail.copy(description = it))},
                    label = { Text(stringResource(id = R.string.description)) },
                    singleLine = true,
                    isError = false
                )

                when (info) {
                    true -> {
                        TextButton(onClick = onDismissRequest ) {
                            Text(text = stringResource(id = R.string.ok))
                        }
                    }
                    false -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onDismissRequest ) {
                                Text(text = stringResource(id = R.string.cancel))
                            }

                            TextButton(onClick = saveArrival,
                                enabled = transactionData.piece.isNotBlank() && stringToFloat(transactionData.piece) != null && transactionData.unitPrice.isBlank() || stringToBigDecimal(transactionData.unitPrice) != null && stringToFloat(transactionData.piece) != null
                                ) {
                                Text(text = stringResource(id = R.string.ok))
                            }
                        }
                    }
                }

            }
        }
    }

}