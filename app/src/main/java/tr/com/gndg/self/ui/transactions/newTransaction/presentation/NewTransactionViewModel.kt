package tr.com.gndg.self.ui.transactions.newTransaction.presentation


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tr.com.gndg.self.core.util.StockOperation
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.core.util.stringToFloat
import tr.com.gndg.self.domain.join.toTransactionState
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.model.transactions.TransactionState
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository
import tr.com.gndg.self.ui.transactions.newTransaction.NewTransactionScreenDestination
import tr.com.gndg.self.ui.transactions.newTransaction.TransactionPortalDestination
import tr.com.gndg.self.ui.transactions.newTransaction.TransactionWarehouseSelectScreenDestination
import java.util.UUID

class NewTransactionViewModel(
    savedStateHandle: SavedStateHandle,
    val warehouseRepository: WarehouseRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    var transactionTypeArg :Int? by mutableStateOf(null)
    var transactionTypeForWarehouseArgs :Int? by mutableStateOf(null)
    private var transactionIDArgs :Long? by mutableStateOf(null)
    var showToast by mutableStateOf(false)
    var toastMessage : String = ""

    private val _warehouseUiState = warehouseRepository.warehouseUiState
    val warehouseUiState get() = _warehouseUiState.asStateFlow()


    private val _repositoryTransactionState = transactionRepository.transactionState
    val repositoryTransactionState get() = _repositoryTransactionState.asStateFlow()


    private var _warehouseList = MutableStateFlow<List<Warehouse>>(listOf())
    val warehouseList get() = _warehouseList.asStateFlow()

    var openDialogDeleteTransactionFail by mutableStateOf(false)
    var openDialogDeleteTransactionConfirm by mutableStateOf(false)

    init {
        transactionTypeArg = savedStateHandle[TransactionPortalDestination.transactionTypeArgs]
        transactionTypeForWarehouseArgs = savedStateHandle[TransactionWarehouseSelectScreenDestination.transactionTypeForWarehouseArgs]
        transactionIDArgs = savedStateHandle[NewTransactionScreenDestination.transactionIDArg]
    }

    fun resetTransaction() = transactionRepository.resetTransactionState()

    fun getWarehouses()  {
        viewModelScope.launch(Dispatchers.IO) {
            _warehouseList.value = warehouseRepository.getWarehouses()
        }
    }

    fun getTransaction() {
        if (transactionIDArgs != null && transactionIDArgs != 0L) {
            viewModelScope.launch(Dispatchers.IO) {
                transactionRepository.getTransactionJoinDataByID(transactionIDArgs!!)?.toTransactionState()?.let {
                    // Yeniden çekiyor. Aynı ise yeniden çekmesin. Değişiklik yaparsak ne olacak ?
                    // state
                    transactionRepository.setTransactionState(it)

                }
            }
        }
    }

    fun setTransactionDetail(transactionDetail: TransactionDetail) = transactionRepository.setTransactionDetail(transactionDetail)

    fun deleteProductFromList(transactionDataDetail: TransactionDataDetail) {
        if (transactionIDArgs != null && transactionIDArgs != 0L) {
            viewModelScope.launch(Dispatchers.IO) {
                val detail = transactionRepository.transactionState.value.transactionDetail
                when(detail.transactionType) {
                    TransactionType.Arrival -> {
                        if (detail.targetUUID != null) {
                            transactionRepository.checkStockForDelete(transactionDataDetail.productUUID, detail.targetUUID!!,
                                stringToFloat( transactionDataDetail.piece)
                            ).onSuccess {stock->
                                // remove stock from warehouse
                                transactionRepository.updateStock(stock, transactionDataDetail.piece, StockOperation.REMOVE).let {
                                    //delete transaction data
                                    transactionRepository.deleteTransactionDataByUUID(transactionDataDetail.uuid).also {
                                        //get new Transaction
                                        getTransaction()
                                    }

                                }
                            }.onFailure {
                                openDialogDeleteTransactionFail = true
                            }
                        } else {
                            Log.e("deleteProductFromList", "Arrival Target NULL")
                        }

                    }

                    TransactionType.Outgoing -> {
                        if (detail.sourceUUID != null) {
                            transactionRepository.getStock(transactionDataDetail.productUUID,
                                detail.sourceUUID)
                                .let { stock->
                                    if (stock != null) {
                                        // added stock to warehouse
                                        transactionRepository.updateStock(stock, transactionDataDetail.piece, StockOperation.ADD).let {
                                            //delete transaction data
                                            transactionRepository.deleteTransactionDataByUUID(transactionDataDetail.uuid).also {
                                                //get new Transaction
                                                getTransaction()
                                            }
                                    }
                                } else {
                                        val newStock = Stock(
                                            id = null,
                                            uuid = UUID.randomUUID().toString(),
                                            warehouseUUID = detail.targetUUID!!,
                                            productUUID = transactionDataDetail.productUUID,
                                            warehouseID = detail.targetID!!,
                                            productID = transactionDataDetail.productID,
                                            piece = transactionDataDetail.piece.toFloat()
                                        )
                                        transactionRepository.addStock(newStock).also {
                                            //get new Transaction
                                            getTransaction()
                                        }
                                    }
                            }
                        } else {
                            Log.e("deleteProductFromList", "Outgoing Source NULL")
                        }

                    }
                    TransactionType.Transfer -> {
                        if (detail.sourceUUID != null && detail.targetUUID != null) {
                            transactionRepository.checkStockForDelete(transactionDataDetail.productUUID,
                                detail.targetUUID!!,
                                stringToFloat( transactionDataDetail.piece)
                            ).onSuccess { stock->
                                // added stock to warehouse
                                transactionRepository.updateStock(stock, transactionDataDetail.piece, StockOperation.REMOVE)


                                //add stock to source warehouse
                                transactionRepository.getStock(transactionDataDetail.productUUID,
                                    detail.sourceUUID)?.let {sourceStock->

                                    transactionRepository.updateStock(sourceStock, transactionDataDetail.piece, StockOperation.ADD).let {
                                        //delete transaction data
                                        transactionRepository.deleteTransactionDataByUUID(transactionDataDetail.uuid).also {
                                            //get new Transaction
                                            getTransaction()
                                        }
                                    }
                                }

                            }.onFailure {
                                openDialogDeleteTransactionFail = true
                            }

                        } else {
                            Log.e("deleteProductFromList", "Transfer Source and Target NULL")
                        }

                    }

                }

            }
        } else {
            val dataList = _repositoryTransactionState.value.dataList.also {
                it.remove(transactionDataDetail)
            }
            val set = repositoryTransactionState.value.copy(dataList= dataList)
            setTransactionState(set)
        }

    }

    suspend fun getLastTransactionID() : String = withContext(Dispatchers.IO){
             (transactionRepository.getLastTransactionID()?.plus(1)?:1).toString()
    }

    suspend fun saveTransaction() : Result<Boolean> = withContext(Dispatchers.IO){

            // Bu transaction state içinde olduğundan direkt onu kaydettik
            return@withContext transactionRepository.insertDbNewTransaction(repositoryTransactionState.value)
                .onSuccess {
                    //kayıt başarı ise bu transaction i state eşledik ki oradaki transactionID 0 olmayacağından
                    // güncelleme mi yoksa yeni kayıtmı anlayabileeğiz !
                transactionRepository.getTransactionJoinDataByUUID(repositoryTransactionState.value.uuid)?.let {

                    transactionIDArgs = it.transaction.transactionID

                    setTransactionState(it.toTransactionState())
                }
            }
                .onFailure {
                Log.e("NewTransactionVM", it.message.toString())
            }

    }

    private fun setTransactionState(transactionState: TransactionState) = transactionRepository.setTransactionState(transactionState)

    suspend fun updateTransactionDetail() : Result<Boolean> = withContext(Dispatchers.IO) {
        transactionRepository.updateTransactionDetail(repositoryTransactionState.value.transactionDetail)
    }

    suspend fun transactionDeleteRequest() : Result<String> = withContext(Dispatchers.IO) {
        transactionRepository.transactionDeleteRequest(transactionState = repositoryTransactionState.value)
    }


    suspend fun deleteTransaction(transactionState: TransactionState) : Result<Boolean> =
        withContext(Dispatchers.IO)
        { transactionRepository.deleteTransaction(transactionState) }

}