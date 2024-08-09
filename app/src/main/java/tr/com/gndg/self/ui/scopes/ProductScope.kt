package tr.com.gndg.self.ui.scopes

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tr.com.gndg.self.R
import tr.com.gndg.self.domain.repo.ProductRepository

@Composable
fun ProductNameText(
    productUUID: String?,
    productID: Long?,
    modifier : Modifier
) {

    var productName by remember {
        mutableStateOf("")
    }

    val productRepository : ProductRepository = koinInject()

    LaunchedEffect(key1 = productName) {
        this.launch(Dispatchers.IO) {
            if (productID != null) {
                 productRepository.getProduct(productID).collect {
                    it.onSuccess {result->
                    result?.let {product->
                        productName = product.name
                    }

                    }
                }
            }
            if (productUUID != null) {
                productRepository.getProductUUID(productUUID).collect {
                    it.onSuccess { result->
                        result?.let {product->
                            productName = product.name
                        }
                    }
                }
            }

        }
    }

    Text(
        text = productName,
        modifier = modifier
    )

}

@Composable
fun ProductImage(
    productUUID: String
){
    var productImageUri: Uri? by remember {
        mutableStateOf(null)
    }

    val productRepository : ProductRepository = koinInject()

    LaunchedEffect(key1 = productImageUri) {
        this.launch(Dispatchers.IO) {
            productRepository.getProductImage(productUUID)?.let {
                   if (productImageValid(it)) {
                       productImageUri = Uri.parse(it)
                   }
            }

        }
    }

    Box(modifier = Modifier
        .size(54.dp, 54.dp)
    ) {
        AsyncImage(
            model = productImageUri?:R.drawable.baseline_image_white_54,
            contentDescription= "Product Image",
            placeholder = painterResource(R.drawable.baseline_image_white_54)
        )
    }
}

fun productImageValid(imageUri: String) : Boolean {
    return try {
        Uri.parse(imageUri).encodedPath != null
    } catch (e: Exception) {
        Log.e("productImageValid", e.message.toString())
        false
    }
}