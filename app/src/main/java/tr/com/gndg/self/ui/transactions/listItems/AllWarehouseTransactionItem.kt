package tr.com.gndg.self.ui.transactions.listItems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import org.joda.time.DateTime
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.core.util.returnTypeString
import tr.com.gndg.self.domain.join.TransactionJoinData
import tr.com.gndg.self.ui.scopes.SourceAndTargetNameText
import tr.com.gndg.self.ui.scopes.WarehouseNameText
import tr.com.gndg.self.ui.texts.TransactionStockPieceText


@Composable
fun AllWarehouseTransactionItem(
    transactionJoinData: TransactionJoinData,
    modifier : Modifier
) {
    val context = LocalContext.current
    val warehouseUUID = when(transactionJoinData.transaction.transactionType) {
        TransactionType.Arrival-> {
            transactionJoinData.transaction.targetUUID
        }
        TransactionType.Outgoing-> {
            transactionJoinData.transaction.sourceUUID
        }
        TransactionType.Transfer-> {
            transactionJoinData.transaction.sourceUUID
        } else -> {
            null
        }
    }

    val sourceTargetUUID = when(transactionJoinData.transaction.transactionType) {
        TransactionType.Arrival-> {
            transactionJoinData.transaction.sourceUUID //supplier
        }
        TransactionType.Outgoing-> {
            transactionJoinData.transaction.targetUUID // customer
        }
        TransactionType.Transfer-> {
            transactionJoinData.transaction.targetUUID // other warehouse
        } else -> {
            null
        }
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(dimensionResource(id = R.dimen.padding_extra_small))
    )
    {
        Card {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                headlineContent = {
                    Text(
                        text = returnTypeString(context, transactionJoinData.transaction.transactionType)
                    )
                },
                supportingContent = {
                    Column {
                        Text(text = DateTime(transactionJoinData.transaction.date).toString("dd.MM.yyyy"))

                        if (sourceTargetUUID != null) {
                            SourceAndTargetNameText(
                                transactionType = transactionJoinData.transaction.transactionType,
                                sourceTargetUUID = sourceTargetUUID
                            )
                        }

                    }

                },
                trailingContent = {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WarehouseNameText(
                            modifier = Modifier,
                            warehouseUUID = warehouseUUID,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center)

                        TransactionStockPieceText(
                            modifier = Modifier,
                            textStyle = MaterialTheme.typography.bodyLarge,
                            transactionJoinData.dataList
                        )
                    }

                },
            )
        }
    }

}