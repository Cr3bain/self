package tr.com.gndg.self.domain.join

import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.ProductDetails
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.model.toProduct

data class ProductsJoin(
    val product : Product,
    val stock: List<Stock>,
    val productTransactions: List<ProductTransaction>,
    val categoryName: String?,
    val imageUri: String?,
    val brandDataName: String?,
    val sizeDataName: String?,
    val labelDataName: String?
    )

data class ProductsJoinUiState(
    val product: Product = ProductDetails().toProduct(),
    val stock: List<Stock> = listOf(),
    val productTransactions: List<ProductTransaction> = listOf(),
    val categoryName: String? = null,
    val imageUri: String? = null,
    val brandDataName: String? = null,
    val sizeDataName: String? = null,
    val labelDataName: String? = null
)

fun ProductsJoin.toProductJoinUiState() = ProductsJoinUiState(
    product = this.product,
    stock = this.stock,
    productTransactions= this.productTransactions,
    categoryName = this.categoryName,
    imageUri = this.imageUri,
    brandDataName = this.brandDataName,
    sizeDataName = this.sizeDataName,
    labelDataName = this.labelDataName
)

fun ProductsJoinUiState.toProductsJoin() = ProductsJoin(
    this.product,
    this.stock,
    this.productTransactions,
    this.categoryName,
    this.imageUri,
    this.brandDataName,
    this.sizeDataName,
    this.labelDataName
)