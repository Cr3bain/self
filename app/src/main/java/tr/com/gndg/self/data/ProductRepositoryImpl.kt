package tr.com.gndg.self.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.domain.join.ProductsJoinUiState
import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.ProductUiState
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.repo.ProductRepository
import java.math.BigDecimal


class ProductRepositoryImpl(private val dataSourceRepository: DataSourceRepository) : ProductRepository {

    val tag = "ProductRepositoryImpl"

    private var _productUiState = MutableStateFlow(ProductUiState())

    //private var _productsJoinList = MutableStateFlow(emptyList<ProductsJoin>())

    override val productUiState: MutableStateFlow<ProductUiState>
        get() = _productUiState


    private var _productsJoinUiState = MutableStateFlow(ProductsJoinUiState())

    override val productsJoinUiState: MutableStateFlow<ProductsJoinUiState>
        get() = _productsJoinUiState

    override fun setProductUiState(productUiState: ProductUiState) {
        println("setProductUiState ProductRepositoryImp")
        _productUiState.value = productUiState
    }

    override fun setProductsJoinUiState(productsJoinUiState: ProductsJoinUiState) {
        println("setProductsJoinUiState ProductRepositoryImp")
        _productsJoinUiState.value = productsJoinUiState
    }

    override fun getProduct(id: Long): Flow<Result<Product?>> {
        return dataSourceRepository.getProduct(id)
    }

    override fun getProductUUID(uuid: String): Flow<Result<Product?>> {
        return dataSourceRepository.getProductUUID(uuid)
    }

    override fun getProducts(): Flow<Result<List<Product>>> {
        return dataSourceRepository.getProducts()
    }

    override fun setPrice(id: Long, newPrice: BigDecimal) : Result<Boolean> {
        return dataSourceRepository.setPrice(id, newPrice)
    }

    override suspend fun updateProduct(product: Product) : Result<Boolean> {
        return dataSourceRepository.updateProduct(product)
    }

    override suspend fun insertProduct(product: Product): Result<Boolean>{
        return dataSourceRepository.insertProduct(product)
    }

    override suspend fun deleteProduct(product: Product): Result<Boolean> {
        return dataSourceRepository.deleteProduct(product)
    }

    override fun getProductsJoin() : Flow<Result<List<ProductsJoin>>> {
        return dataSourceRepository.getProductsJoin()
    }

    override fun getProductJoin(productUUID: String): Flow<Result<ProductsJoin?>> {
        val data = dataSourceRepository.getProductJoin(productUUID)
        return if (data != null) {
            flow {
                emit(Result.success(data))
            }
        } else {
            flow {
                emit(Result.failure(Throwable("null")))
            }
        }

    }
    override suspend fun getProductStocks(productUUID: String): List<Stock> {
        return dataSourceRepository.getProductStocks(productUUID)
    }

    override suspend fun getProductByBarcode(barcode: String): Product? {
        return dataSourceRepository.getProductByBarcode(barcode)
    }

    override suspend fun setProductImage(productUUID: String, imageUri: String?) {
        return dataSourceRepository.setProductImage(productUUID, imageUri)
    }

    override suspend fun getProductImage(productUUID: String): String? {
        return dataSourceRepository.getProductImage(productUUID)
    }

    override suspend fun deleteImageUri(productUUID: String, imageUri: String?) : Result<Boolean> {
        return dataSourceRepository.deleteImageUri(productUUID, imageUri)
    }

}