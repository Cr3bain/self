package tr.com.gndg.self.domain.repo

import kotlinx.coroutines.flow.MutableStateFlow
import tr.com.gndg.self.core.util.StockOperation
import tr.com.gndg.self.domain.join.ProductTransaction
import tr.com.gndg.self.domain.join.TransactionJoinData
import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.model.transactions.Transaction
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.model.transactions.TransactionState

interface TransactionRepository {

    val transactionState : MutableStateFlow<TransactionState>

    fun setTransactionState(transactionState: TransactionState)

    fun transactionWarehouseUUIDByTypes() : String?

    fun transactionWarehouseIDByTypes() : Long?

    suspend fun getLastTransactionID() : Long?

    suspend fun getTransactions(): List<Transaction>

    suspend fun getTransactionJoinDataByID(transactionID: Long) : TransactionJoinData?

    suspend fun getTransactionJoinDataByUUID(transactionUUID: String) : TransactionJoinData?

    suspend fun updateTransactionDetail(transactionDetail: TransactionDetail) : Result<Boolean>

    fun resetTransactionState()

    fun setTransactionDetail(transactionDetail: TransactionDetail)

    fun setTransactionDataListDetail(dataList: MutableList<TransactionDataDetail>)

    suspend fun insertDbNewTransaction(transactionState: TransactionState) : Result<Boolean>

    suspend fun insertDbTransactionData(transactionState: TransactionState, transactionDataDetail: TransactionDataDetail) : Result<Boolean>

    suspend fun updatePurchasePrice(productUUID: String, newUnitPrice: String)

    suspend fun getStock(productUUID: String, warehouseUUID: String): Stock?

    suspend fun checkStockForDelete(productUUID: String, warehouseUUID: String, piece: Float?): Result<Stock>

    suspend fun getProduct(productUUID: String) : Product?

    suspend fun addStock(stock: Stock)

    suspend fun updateStock(stock: Stock, piece: String, stockOperation: StockOperation)

    suspend fun getProductTransactions(productUUID: String) : List<ProductTransaction>

    suspend fun getTransactionJoinDataList() : List<TransactionJoinData>

    suspend fun transactionDeleteRequest(transactionState: TransactionState) : Result<String>

    suspend fun deleteTransaction(transactionState: TransactionState): Result<Boolean>

    suspend fun deleteAllTransactionsDataByTransactionUUID(transactionUUID : String) : Result<Boolean>

    suspend fun deleteTransactionByUUID(uuid : String): Result<Boolean>

    suspend fun deleteTransactionDataByUUID(transactionDataUUID : String) : Result<Boolean>


}