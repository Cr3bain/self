package tr.com.gndg.self.domain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.domain.join.ProductsJoinUiState
import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.ProductUiState
import tr.com.gndg.self.domain.model.Stock
import java.math.BigDecimal

interface ProductRepository {

    val productUiState : MutableStateFlow<ProductUiState>

    val productsJoinUiState : MutableStateFlow<ProductsJoinUiState>


    fun setProductUiState(productUiState: ProductUiState)

    fun setProductsJoinUiState(productsJoinUiState: ProductsJoinUiState)

    fun getProduct(id: Long) : Flow<Result<Product?>>

    fun getProductUUID(uuid: String) : Flow<Result<Product?>>

    fun getProducts() : Flow<Result<List<Product>>>

    fun setPrice(id: Long, newPrice: BigDecimal): Result<Boolean>

    suspend fun updateProduct(product: Product): Result<Boolean>

    suspend fun insertProduct(product: Product): Result<Boolean>

    suspend fun deleteProduct(product: Product) : Result<Boolean>

    fun getProductsJoin() : Flow<Result<List<ProductsJoin>>>

    fun getProductJoin(productUUID: String) : Flow<Result<ProductsJoin?>>

    suspend fun getProductStocks(productUUID: String) : List<Stock>

    suspend fun getProductByBarcode(barcode: String): Product?

    suspend fun setProductImage(productUUID: String, imageUri: String?)

    suspend fun getProductImage(productUUID: String) : String?

    suspend fun deleteImageUri(productUUID: String, imageUri: String?) : Result<Boolean>

}