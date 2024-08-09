package tr.com.gndg.self.ui.product.stateScreens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.floatToString
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.ui.texts.ProductStockText


@Composable
fun StockStateScreen(
    modifier : Modifier,
    stockList: List<Stock>,
    warehouseList: List<Warehouse>,
    arrivalStockWarehouse: (Warehouse) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {

            LazyColumn(
                modifier= Modifier
                    .height(this.maxHeight)
                    .fillMaxSize()

            ) {
                var totalStock = 0F
                stockList.forEach {
                    if (it.piece != null) {
                        totalStock += it.piece
                    }

                }

                item {
                    Spacer(modifier = Modifier.size(10.dp))
                }

                item {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        headlineContent = {
                            Text(text = stringResource(id = R.string.total), style = MaterialTheme.typography.titleMedium)
                        },
                        trailingContent = {
                            Text(text = floatToString(totalStock), style = MaterialTheme.typography.bodyLarge)
                        }
                    )
                }

                items(warehouseList) {
                    StockStateListItem(it, stockList) { arrivalStockWarehouse(it) }
                }
            }
        }
}

@Composable
fun StockStateListItem(
    warehouse: Warehouse,
    stockList: List<Stock>,
    arrival: () -> Unit
) {

    ListItem(
        leadingContent = {
            FilledTonalIconButton(onClick = {
                arrival()
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "add stock")
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        headlineContent = {
            Text(text = warehouse.name, style = MaterialTheme.typography.titleMedium)
        },
        trailingContent = {
            ProductStockText(warehouseUUID = warehouse.uuid, stockList = stockList)
        }
        )
}