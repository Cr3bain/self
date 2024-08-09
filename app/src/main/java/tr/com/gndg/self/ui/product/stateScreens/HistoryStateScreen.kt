package tr.com.gndg.self.ui.product.stateScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.joda.time.DateTime
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.core.util.returnTypeString
import tr.com.gndg.self.domain.join.ProductTransaction
import tr.com.gndg.self.domain.model.transactions.TransactionState
import tr.com.gndg.self.domain.model.transactions.toTransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.toTransactionDetail
import tr.com.gndg.self.ui.dialogs.ArrivalProductDialog
import tr.com.gndg.self.ui.scopes.WarehouseNameText

@Preview
@Composable
fun HistoryStatePreview(){
    HistoryStateScreen(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), productTransactionList = emptyList()
    )
}

@Composable
fun HistoryStateScreen(
    modifier: Modifier,
    productTransactionList: List<ProductTransaction>
) {
    BoxWithConstraints(
        modifier = modifier
    ) {

        if (productTransactionList.isEmpty()) {

            Text(
                modifier = Modifier
                    .height(this.maxHeight)
                    .fillMaxWidth(),
                text = stringResource(R.string.noHistory),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

        } else {
            LazyColumn(modifier= Modifier
                .height(this.maxHeight)
                .fillMaxSize()
            ) {
                items(productTransactionList.sortedByDescending { it.transaction?.date }) {
                    ProductTransactionListItem(it)
                }
            }
        }

    }

}

@Composable
fun ProductTransactionListItem(productTransaction: ProductTransaction) {

    ArrivalListItem(productTransaction)

}

@Composable
fun ArrivalListItem(productTransaction: ProductTransaction) {
    var showInfo by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val sourceTargetUUID = when(productTransaction.transaction?.transactionType) {
        TransactionType.Arrival-> {
            productTransaction.transaction.targetUUID //supplier
        }
        TransactionType.Outgoing-> {
            productTransaction.transaction.sourceUUID // customer
        }
        TransactionType.Transfer-> {
            productTransaction.transaction.sourceUUID // other warehouse
        } else -> {
            null
        }
    }

    ListItem(
        leadingContent = {
            Text(text = productTransaction.transactionData?.piece.toString(),
                style = MaterialTheme.typography.titleLarge)

        },
        overlineContent = {
            Row {
                Text(modifier = Modifier.padding(end = 10.dp),
                    text = returnTypeString(context, productTransaction.transaction?.transactionType)
                )
                Text(text = DateTime(productTransaction.transaction?.date).toString("dd.MM.yyyy"))
            }

        },
        headlineContent = {
            WarehouseNameText(
                modifier = Modifier,
                warehouseUUID = sourceTargetUUID,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = null)
        },

        trailingContent =  {
            IconButton(onClick = { showInfo = true }) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "Arrival Info")
            }
        }
        )

    when  {
        showInfo -> {
            if (productTransaction.transactionData != null && productTransaction.transaction != null) {
                ArrivalProductDialog(
                    info = true,
                    transactionState = TransactionState(
                        uuid = productTransaction.transaction.uuid,
                        transactionDetail = productTransaction.transaction.toTransactionDetail(),
                        dataList = mutableListOf(productTransaction.transactionData.toTransactionDataDetail())
                    ),
                    onDismissRequest = {  showInfo = false },
                    transactionDataChange = {},
                    transactionDetailChange = {},
                    datePickerDialog = {  },
                    toSupplierScreen = { },
                    saveArrival = {}
                )
            }

        }
    }

}