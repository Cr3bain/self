package tr.com.gndg.self.ui.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import tr.com.gndg.self.R
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.ui.scopes.ProductImage
import tr.com.gndg.self.ui.texts.ProductStockText


@Composable
fun ProductsJoinList(
    productItemList: List<ProductsJoin>,
    selectedProductsJoin: (ProductsJoin) -> Unit,
    modifier: Modifier = Modifier,
    warehouseUUID: String?

) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {

        items(productItemList, key = {it.product.uuid }) {productItem->
            ProductItem(item = productItem,
                warehouseUUID = warehouseUUID,
                modifier = Modifier
                    //.padding(dimensionResource(id = R.dimen.padding_extra_small))
                    .clickable { selectedProductsJoin(productItem) })

        }
    }

}

@Composable
private fun ProductItem(
    item: ProductsJoin,
    modifier: Modifier,
    warehouseUUID: String?
) {

    Box(modifier = modifier
        .padding(dimensionResource(id = R.dimen.padding_extra_small))) {
        Card {
            ListItem(
                leadingContent = {

                    ProductImage(productUUID = item.product.uuid)

                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                headlineContent = { Text(text = item.product.name,
                    style = MaterialTheme.typography.titleMedium) },
                supportingContent = {
                    if (item.product.barcode != null) {
                        Text(text = item.product.barcode,
                            style = MaterialTheme.typography.labelMedium)
                    }

                },
                trailingContent = {
                    ProductStockText(
                        warehouseUUID = warehouseUUID,
                        stockList = item.stock
                    )

                },

                )
        }
    }
}