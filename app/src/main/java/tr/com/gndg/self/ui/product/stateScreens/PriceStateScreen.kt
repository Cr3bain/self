package tr.com.gndg.self.ui.product.stateScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.bigDecimalToString
import tr.com.gndg.self.core.util.stringToBigDecimal
import tr.com.gndg.self.domain.model.ProductDetails


@Composable
fun PriceStateScreen(
    modifier : Modifier,
    productDetails: ProductDetails,
    onValueChange: (ProductDetails) -> Unit = {},
){

    Column(
        modifier = modifier
    ){


/*        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = bigDecimalToString(productDetails.minStock),
            onValueChange = { onValueChange(productDetails.copy(minStock = stringToBigDecimal(it))) },
            label = { Text(stringResource(id = R.string.minStock)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = false,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.stocks_48),
                    contentDescription = "Minimum Stock",
                )
            },
            trailingIcon = {
                IconButton(onClick = {

                }) {

                }
                Icon(imageVector = Icons.Default.Done, contentDescription = "Done")

            },

            )*/

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = bigDecimalToString(productDetails.purchasePrice),
                onValueChange = { onValueChange(productDetails.copy(purchasePrice = stringToBigDecimal(it))) },
                label = { Text(stringResource(id = R.string.purchasePrice)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = false,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.unpacking_48),
                        contentDescription = "Purchase Price",
                    )
                },

                )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = bigDecimalToString(productDetails.sellPrice),
                onValueChange = { onValueChange(productDetails.copy(sellPrice = stringToBigDecimal(it))) },
                label = { Text(stringResource(id = R.string.sellPrice)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = false,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.packing_48),
                        contentDescription = "Sell Price",
                    )
                },

                )


    }

}