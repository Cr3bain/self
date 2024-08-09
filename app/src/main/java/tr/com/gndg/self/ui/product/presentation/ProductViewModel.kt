package tr.com.gndg.self.ui.product.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tr.com.gndg.self.R
import tr.com.gndg.self.domain.join.ProductsJoinUiState
import tr.com.gndg.self.domain.join.toProductJoinUiState
import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.ProductDetails
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.toProduct
import tr.com.gndg.self.domain.model.toProductDetails
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.repo.BarcodeScannerRepository
import tr.com.gndg.self.domain.repo.ProductRepository
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository
import tr.com.gndg.self.ui.product.ProductDestination
import java.io.File


class ProductViewModel (
    savedStateHandle: SavedStateHandle,
    private val scanner: BarcodeScannerRepository,
    private val repository: ProductRepository,
    private val warehouseRepository: WarehouseRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private var _uuidArg :String? by mutableStateOf(null)

    private var _repoProductJoinUiState = repository.productsJoinUiState


    var productsJoinUiState by mutableStateOf(ProductsJoinUiState())
        private set


    var productScreenUiState by mutableStateOf(ProductScreensUiState())
        private set

    var somethingChanged by mutableStateOf(false)

    var warehouseListState by mutableStateOf<List<Warehouse>>(emptyList())
        private set

    private val _repoTransactionState = transactionRepository.transactionState
    val repoTransactionState get() = _repoTransactionState.asStateFlow()


    var arrivalProductDialog by mutableStateOf(false)

    var datePickerDialog by mutableStateOf(false)

    var showToast by mutableStateOf(false)
    var toastMessage : String = ""
    var openDialogDeleteProductFail by mutableStateOf(false)
    var openDialogDeleteProdcutConfirm by mutableStateOf(false)

    init {
        _uuidArg = savedStateHandle[ProductDestination.uuidArg]
        //transactionRepository.resetTransactionState()
    }

    fun resetTransactionState() = transactionRepository.resetTransactionState()

    private fun getWarehouseList() {
        viewModelScope.launch(Dispatchers.IO) {
            warehouseListState = warehouseRepository.getWarehouses()
        }

    }

    fun uuidArgCollect() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_uuidArg.isNullOrEmpty()) {
                getProduct(_uuidArg!!)
            } else {
                changeProductScreenState(
                    ProductScreensUiState(
                        newProduct = true,
                        showButtons = false,
                        showScreenName = ""
                    )
                )
            }
        }
    }

    private fun getProduct(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProductJoin(uuid).collectLatest {
                it.onSuccess { productsJoinNull->
                    productsJoinNull?.let { productJoin->
                        repository.setProductsJoinUiState(productJoin.toProductJoinUiState())

                        // that's my boy...
                        productsJoinUiState = productJoin.toProductJoinUiState()


                        // not new product, set Product state
                        changeProductScreenState(
                            productScreenUiState.copy(newProduct = false, showButtons = true, showScreenName = "price")

                        )
                        //get product transaction history
                        //getProductTransactions()
                    }
                }
                it.onFailure {e->
                    println(e.message.toString())
                }
            }
            getWarehouseList()
        }
    }

    fun setProductJoinUiStateProduct(productDetails: ProductDetails) {
        somethingChanged = true
        productsJoinUiState = productsJoinUiState.copy(product = productDetails.toProduct())

    }

    fun deleteImage(imageUri: String) {
        viewModelScope.launch {
            try {
                val uriPath = Uri.parse(imageUri).encodedPath
                if (uriPath != null) {
                    File(uriPath).delete()
                }

            } catch (e: Exception) {
                Log.e("deleteImage", e.message.toString())
            }

            this.launch(Dispatchers.IO) {
                repository.deleteImageUri(productsJoinUiState.product.uuid, imageUri)
            }
            productsJoinUiState = productsJoinUiState.copy(imageUri = null)
        }

    }

    fun setProductImageUri(productUUID: String, imageUri: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setProductImage(
                productUUID = productUUID,
                imageUri = imageUri
            ).let {
                productsJoinUiState = productsJoinUiState.copy(imageUri = imageUri)
            }

            this.launch(Dispatchers.Main) {

            }
        }

    }

    fun startScanning(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            scanner.startScanning().collect {
                it?.let {
                    getProductByBarcode(it).let {product->
                        if (product == null) {
                            setProductJoinUiStateProduct(
                                productsJoinUiState.product.toProductDetails().copy(barcode = it)
                            )

                        } else {
                            toastMessage = context.getString(R.string.barcodeUsing)
                            showToast = true
                        }
                    }

                }
            }
        }
    }

    private suspend fun getProductByBarcode(barcode: String) : Product? = withContext(Dispatchers.IO){
        repository.getProductByBarcode(barcode)
    }


    fun setTransactionDataListDetail(transactionDataDetail: TransactionDataDetail) {
        transactionRepository.setTransactionDataListDetail(mutableListOf(transactionDataDetail))
    }

    fun setTransactionDetail(transactionDetail: TransactionDetail) {
        transactionRepository.setTransactionDetail(transactionDetail)
    }

    fun showArrivalScreen() {
        arrivalProductDialog = !arrivalProductDialog
    }


    fun changeProductScreenState(productScreenState: ProductScreensUiState) {
        productScreenUiState = productScreenState
    }

    private suspend fun valid(uiState: ProductDetails = _repoProductJoinUiState.value.product.toProductDetails(), context: Context): Result<Boolean> {
        return with(uiState) {
            //repoProductUiState.value.productDetails != uiState
            when {
                name.isBlank() -> {
                    Result.failure(Throwable(context.getString(R.string.nameIsNull)))
                }
                barcode?.let { getProductByBarcode(it) } != null && barcode.let { getProductByBarcode(it)?.uuid } != uuid -> {
                    Result.failure(Throwable(context.getString(R.string.barcodeUsing)))
                } else -> {
                Result.success(true)
                }
            }
     }
    }

    suspend fun saveProduct(context: Context) : Result<String> = withContext(Dispatchers.IO){
        val check = valid(productsJoinUiState.product.toProductDetails(), context)
        return@withContext  if (check.isSuccess) {

            if (!_uuidArg.isNullOrEmpty()) {
                repository.updateProduct(productsJoinUiState.product)
                getProduct(productsJoinUiState.product.uuid)
                somethingChanged = false
                Result.success(context.getString(R.string.productUpdated))
            } else {
                repository.insertProduct(productsJoinUiState.product)
                _uuidArg = productsJoinUiState.product.uuid
                getProduct(productsJoinUiState.product.uuid)
                somethingChanged = false
                Result.success(context.getString(R.string.productSaved))
            }
        } else {
            Result.failure(check.exceptionOrNull()?:Throwable(context.getString(androidx.compose.ui.R.string.default_error_message)))
        }
    }

    suspend fun deleteProduct() : Result<Boolean> = withContext(Dispatchers.IO) {
        repository.deleteProduct(productsJoinUiState.product)
    }

    fun saveArrival() {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.insertDbNewTransaction (repoTransactionState.value)
                .onSuccess {
                    updateStockList()
                    toastMessage = "Product arrival is success"
                    showToast = true
                }
                .onFailure {
                    toastMessage = it.message.toString()
                    showToast = true
                }
        }

    }

    private fun updateStockList() {
        viewModelScope.launch(Dispatchers.IO) {
            val newStocks = repository.getProductStocks(productsJoinUiState.product.uuid)
            productsJoinUiState = productsJoinUiState.copy(stock = newStocks)

            //when added a new product, history record must be refresh !
            updateProductTransactions()
        }
    }

    private fun updateProductTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            productsJoinUiState = productsJoinUiState.copy(productTransactions = transactionRepository.getProductTransactions(productsJoinUiState.product.uuid))
        }
    }

    suspend fun getWarehouse(warehouseUUID: String) = warehouseRepository.getWarehouseUUID(warehouseUUID)
}