package tr.com.gndg.self.ui.inventory.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.repo.BarcodeScannerRepository
import tr.com.gndg.self.domain.repo.ProductRepository
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.ui.inventory.InventoryTransactionDestination
import java.util.UUID

class InventoryViewModel (
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository,
    private val scanner: BarcodeScannerRepository,) : ViewModel() {

    private var firstProductList= listOf<ProductsJoin>()
    private var isSearchStarting = true
    var searchText = mutableStateOf("")

    var transactionIDArg :Long? by mutableStateOf(0)

    init {
        transactionIDArg = savedStateHandle[InventoryTransactionDestination.transactionIDArg]
    }

    val transactionState = transactionRepository.transactionState

    suspend fun addProductToTransactionStateList(productsJoin: ProductsJoin, piece: String, unitPrice: String) :
            Result<Boolean> = withContext(Dispatchers.IO){

       try {
           if (transactionState.value.transactionDetail.transactionID !=0L) {
               // saved transaction. add to database
                   TransactionDataDetail(
                       uuid = UUID.randomUUID().toString(),
                       transactionUUID = transactionState.value.transactionDetail.uuid,
                       productUUID = productsJoin.product.uuid,
                       productID = productsJoin.product.id,
                       piece = piece,
                       unitPrice = unitPrice
                   ).let {
                       return@withContext transactionRepository.insertDbTransactionData(transactionState.value, it)
                   }

           } else {
               // Add state... not saved transaction.
               TransactionDataDetail(
                   uuid = UUID.randomUUID().toString(),
                   transactionUUID = transactionState.value.transactionDetail.uuid,
                   productUUID = productsJoin.product.uuid,
                   productID = productsJoin.product.id,
                   piece = piece,
                   unitPrice = unitPrice
               ).let {
                   transactionState.value.dataList.add(it)
                   return@withContext Result.success(true)
               }
           }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun warehouseUUID(): String? = transactionRepository.transactionWarehouseUUIDByTypes()

    private val _productList = MutableStateFlow<List<ProductsJoin>>(emptyList())
    val productList  get() = _productList.asStateFlow()


    private val _errorList = MutableStateFlow<Throwable?>(null)
    val errorList = _errorList.asStateFlow()

    fun productsJoinList() : Job = viewModelScope.launch(context = Dispatchers.IO) {
         productRepository.getProductsJoin().stateIn(scope = this).value
             .onSuccess { t->
                _productList.value = t
         }
             .onFailure {e->
                _errorList.value = e
             }
    }

    fun searchProduct(query : String){

        val listToSearch= if(isSearchStarting){
            _productList.value
        } else{
            firstProductList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                _productList.value=firstProductList
                isSearchStarting=true
                return@launch
            }
            val results=listToSearch.filter {
                it.product.name.contains(query.trim(),true) ||
                        it.product.barcode?.contains(query.trim(),true) == true
            }
            if(isSearchStarting){
                firstProductList=_productList.value
                isSearchStarting=false

            }
            _productList.value= results
        }

    }

    fun startScanning() {
        viewModelScope.launch {
            scanner.startScanning().collect {
                it?.let {
                    searchText.value = it
                    searchProduct(it)
                }
            }
        }
    }

}