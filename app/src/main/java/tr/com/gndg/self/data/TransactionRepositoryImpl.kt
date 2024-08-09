package tr.com.gndg.self.data

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.core.util.StockOperation
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.core.util.stringToBigDecimal
import tr.com.gndg.self.core.util.stringToFloat
import tr.com.gndg.self.domain.join.ProductTransaction
import tr.com.gndg.self.domain.join.TransactionJoinData
import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.model.transactions.Transaction
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.model.transactions.TransactionState
import tr.com.gndg.self.domain.model.transactions.toArrival
import tr.com.gndg.self.domain.model.transactions.toOutgoing
import tr.com.gndg.self.domain.model.transactions.toTransactionData
import tr.com.gndg.self.domain.model.transactions.toTransfer
import tr.com.gndg.self.domain.repo.TransactionRepository
import java.math.BigDecimal
import java.util.UUID

class TransactionRepositoryImpl(private val dataSourceRepository: DataSourceRepository) : TransactionRepository {

    private val tag = "TransactionRepositoryImpl"
    private var _transactionState = MutableStateFlow(TransactionState())

    override val transactionState: MutableStateFlow<TransactionState> get() = _transactionState

    override fun setTransactionState(transactionState: TransactionState) {
        _transactionState.value = transactionState
    }

    override fun transactionWarehouseUUIDByTypes(): String? {
        return when(_transactionState.value.transactionDetail.transactionType) {
            TransactionType.Arrival -> {
                _transactionState.value.transactionDetail.targetUUID
            }
            TransactionType.Outgoing -> {
                _transactionState.value.transactionDetail.sourceUUID

            }
            TransactionType.Transfer -> {
                _transactionState.value.transactionDetail.sourceUUID

            }
            TransactionType.TransferTarget -> {
                _transactionState.value.transactionDetail.targetUUID

            } else -> {
                null
            }
        }
    }

    override fun transactionWarehouseIDByTypes(): Long? {
        return when(_transactionState.value.transactionDetail.transactionType) {
            TransactionType.Arrival -> {
                _transactionState.value.transactionDetail.targetID
            }
            TransactionType.Outgoing -> {
                _transactionState.value.transactionDetail.sourceID

            }
            TransactionType.Transfer -> {
                _transactionState.value.transactionDetail.sourceID

            }
            TransactionType.TransferTarget -> {
                _transactionState.value.transactionDetail.targetID

            } else -> {
                null
            }
        }
    }

    override suspend fun getLastTransactionID(): Long? {
        return dataSourceRepository.getLastTransactionID()
    }

    override suspend fun getTransactions(): List<Transaction> {
        return dataSourceRepository.getTransactions()
    }

    override suspend fun getTransactionJoinDataByID(transactionID: Long): TransactionJoinData? {
        return dataSourceRepository.getTransactionByID(transactionID)
    }

    override suspend fun getTransactionJoinDataByUUID(transactionUUID: String): TransactionJoinData? {
        return dataSourceRepository.getTransactionByUUID(transactionUUID)
    }

    override suspend fun updateTransactionDetail(transactionDetail: TransactionDetail): Result<Boolean> {
        return dataSourceRepository.updateTransactionDetail(transactionDetail)
    }


    override fun resetTransactionState() {
        _transactionState.value = TransactionState()
    }

    override fun setTransactionDetail(transactionDetail: TransactionDetail) {
        _transactionState.value = _transactionState.value.copy(transactionDetail = transactionDetail)
    }

    override fun setTransactionDataListDetail(dataList: MutableList<TransactionDataDetail>) {
        _transactionState.value = _transactionState.value.copy(dataList = dataList)
    }


    override suspend fun insertDbNewTransaction(transactionState: TransactionState): Result<Boolean> {
        return try {
            when(transactionState.transactionDetail.transactionType) {
                TransactionType.Arrival -> {
                    if (transactionState.transactionDetail.targetUUID.isNullOrBlank() || transactionState.transactionDetail.targetID == 0L) {
                        Result.failure(Throwable("Warehouse (TargetID) is 0L"))
                    } else {
                        dataSourceRepository.insertTransaction(transactionState.transactionDetail.toArrival())

                        if (transactionState.dataList.isNotEmpty()) {
                            transactionState.dataList.forEach {transactionDataDetail->
                                insertDbTransactionData(transactionState, transactionDataDetail)
                            }

                        }
                        Result.success(true)
                    }
                }
                TransactionType.Outgoing -> {
                    if (transactionState.transactionDetail.sourceUUID.isNullOrBlank() || transactionState.transactionDetail.sourceID == 0L) {
                        Result.failure(Throwable("Warehouse (SourceID) null"))

                    } else {
                        dataSourceRepository.insertTransaction(transactionState.transactionDetail.toOutgoing())

                        if (transactionState.dataList.isNotEmpty()) {
                            transactionState.dataList.forEach { transactionDataDetail->
                                insertDbTransactionData(transactionState, transactionDataDetail)
                            }
                        }
                        Result.success(true)
                    }
                }
                TransactionType.Transfer -> {
                    if (transactionState.transactionDetail.sourceUUID.isNullOrBlank() || transactionState.transactionDetail.sourceID == 0L
                        && transactionState.transactionDetail.targetUUID.isNullOrBlank() || transactionState.transactionDetail.targetID == 0L) {
                        Result.failure(Throwable("Warehouse (SourceID or Target) null"))

                    } else {
                        dataSourceRepository.insertTransaction(transactionState.transactionDetail.toTransfer())

                        if (transactionState.dataList.isNotEmpty()) {
                            transactionState.dataList.forEach { transactionDataDetail->
                                insertDbTransactionData(transactionState, transactionDataDetail)
                            }
                        } else {
                            Log.v("insertDbNewTransaction", "Product list empty")
                        }
                        Result.success(true)
                    }
                }
                else -> {
                    Result.failure(Throwable("NULL TYPE"))
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStock(productUUID: String, warehouseUUID: String): Stock? {
        return dataSourceRepository.getWarehouseProductStocks(productUUID, warehouseUUID)
    }

    override suspend fun checkStockForDelete(
        productUUID: String,
        warehouseUUID: String,
        piece: Float?
    ): Result<Stock> {
        val stock = getStock(productUUID, warehouseUUID)
        return if (stock?.piece != null && piece != null) {
            if (stock.piece < piece) {
                Result.failure(Throwable("Ürün eksiye düşüyor"))
            } else {
                Result.success(stock)
            }

        } else {
            Result.failure(Throwable("NASIL STOK veya ADET YOK"))
        }
    }


    override suspend fun getProduct(productUUID: String): Product? {
        return dataSourceRepository.getProduct(productUUID)
    }

    override suspend fun addStock(stock: Stock) {
        dataSourceRepository.insertStock(stock)
    }

    override suspend fun updateStock(stock: Stock, piece: String, stockOperation: StockOperation) {
        val statePiece = stringToFloat(piece)
        if (statePiece != null && statePiece != 0F && stock.piece !=null) {
            when(stockOperation) {
                StockOperation.ADD -> {
                    val newPiece = statePiece + stock.piece
                    dataSourceRepository.updateStock(newPiece, stock.uuid)
                }
                StockOperation.REMOVE -> {
                    val newPiece = stock.piece - statePiece
                    dataSourceRepository.updateStock(newPiece, stock.uuid)
                }
            }


        } else {
            Log.e("updateStock", "Stock piece is NULL. WTF")
        }
    }

    override suspend fun insertDbTransactionData(transactionState: TransactionState, transactionDataDetail: TransactionDataDetail) : Result<Boolean> {
        return when(transactionState.transactionDetail.transactionType) {
            TransactionType.Arrival -> {
                if (transactionState.transactionDetail.targetUUID != null) {
                    getStock(transactionDataDetail.productUUID, transactionState.transactionDetail.targetUUID!!).let { stockData->
                        if (stockData == null) {
                            val stock = Stock(
                                id = null,
                                uuid = UUID.randomUUID().toString(),
                                warehouseUUID = transactionState.transactionDetail.targetUUID!!,
                                productUUID = transactionDataDetail.productUUID,
                                warehouseID = transactionState.transactionDetail.targetID!!,
                                productID = transactionDataDetail.productID,
                                piece = transactionDataDetail.piece.toFloat()
                            )
                            addStock(stock)
                        } else {
                            updateStock(stockData, transactionDataDetail.piece, StockOperation.ADD)
                        }
                    }

                    updatePurchasePrice(transactionDataDetail.productUUID, transactionDataDetail.unitPrice)

                    dataSourceRepository.insertTransactionDataList(transactionDataDetail.toTransactionData())
                    Result.success(true)
                } else {
                    Result.failure(Throwable("Warehouse (Target) is NULL"))
                }


            }
            TransactionType.Outgoing -> {
                if (transactionState.transactionDetail.sourceUUID != null) {
                    getStock(transactionDataDetail.productUUID, transactionState.transactionDetail.sourceUUID!!).let {stockData ->
                        if (stockData != null) {
                            updateStock(stockData, transactionDataDetail.piece, StockOperation.REMOVE).also {
                                dataSourceRepository.insertTransactionDataList(transactionDataDetail.toTransactionData())
                            }
                            Result.success(true)
                        } else {
                            Result.failure(Throwable("Stock data is NULL !"))
                        }

                    }
                } else {
                    Result.failure(Throwable("Warehouse (Source) is NULL"))
                }


            }
            TransactionType.Transfer -> {
                if (transactionState.transactionDetail.sourceUUID != null) {
                    getStock(transactionDataDetail.productUUID, transactionState.transactionDetail.sourceUUID!!).let {stockData ->
                        if (stockData != null) {
                            updateStock(stockData, transactionDataDetail.piece, StockOperation.REMOVE).also {
                                dataSourceRepository.insertTransactionDataList(transactionDataDetail.toTransactionData())
                            }
                            Result.success(true)
                        } else {
                            Result.failure(Throwable("Source stock is NULL !"))
                        }

                    }

                } else {
                    Result.failure(Throwable("Warehouse (Source) is NULL"))
                }

                if (transactionState.transactionDetail.targetUUID != null) {
                    getStock(transactionDataDetail.productUUID, transactionState.transactionDetail.targetUUID!!).let {stockData ->
                        if (stockData != null) {
                            updateStock(stockData, transactionDataDetail.piece, StockOperation.ADD).also {
                                dataSourceRepository.insertTransactionDataList(transactionDataDetail.toTransactionData())
                            }
                            Log.v("insertDbTransactionData", "After transfer stock updated ")
                            Result.success(true)
                        } else {
                            val stock = Stock(
                                id = null,
                                uuid = UUID.randomUUID().toString(),
                                warehouseUUID = transactionState.transactionDetail.targetUUID!!,
                                productUUID = transactionDataDetail.productUUID,
                                warehouseID = transactionState.transactionDetail.targetID!!,
                                productID = transactionDataDetail.productID,
                                piece = transactionDataDetail.piece.toFloat()
                            )
                            addStock(stock).also {
                                dataSourceRepository.insertTransactionDataList(transactionDataDetail.toTransactionData())
                            }
                            Log.v("insertDbTransactionData", "After transfer new stock added ")
                            Result.success(true)
                        }

                    }
                } else {
                    Result.failure(Throwable("Warehouse (Target) is NULL"))
                }

            }

            else -> {
                Result.failure(Throwable("transactionType ???"))
            }
        }

    }

    override suspend fun updatePurchasePrice(productUUID: String, newUnitPrice: String) {
        //If product price is null and unitPrice from arrival is not null
        // update to product purchasePrice
        getProduct(productUUID).let { product->
            if (product != null && product.purchasePrice == null) {
                try {
                    val unitPrice = stringToBigDecimal(newUnitPrice)
                    if (unitPrice != null && unitPrice != BigDecimal.ZERO) {
                        dataSourceRepository.updatePurchasePrice(product.uuid,unitPrice)
                    }
                } catch (e: Exception) {
                    Log.e(tag, "unitPrice: ${e.message.toString()} ")
                }

            }
        }
    }

    override suspend fun getProductTransactions(productUUID: String): List<ProductTransaction> {
        return dataSourceRepository.getProductTransactions(productUUID)
    }

    override suspend fun getTransactionJoinDataList(): List<TransactionJoinData> {
        return dataSourceRepository.getTransactionJoinDataList()
    }

    override suspend fun transactionDeleteRequest(transactionState: TransactionState): Result<String> {
        when(transactionState.transactionDetail.transactionType) {
            TransactionType.Arrival -> {
                val warehouseUUID = transactionState.transactionDetail.targetUUID
                if (transactionState.dataList.isEmpty()) {
                    return Result.success("Empty dataList")
                } else if(warehouseUUID != null) {
                    transactionState.dataList.forEach {dataDetail->
                        checkStockForDelete(dataDetail.productUUID, warehouseUUID, stringToFloat(dataDetail.piece))
                            .onFailure {
                                return Result.failure(it)
                            }
                    }.let {
                        return Result.success("OK")
                    }
                } else {
                    return Result.success("OK")
                }
            }
            TransactionType.Outgoing -> {
                val warehouseUUID = transactionState.transactionDetail.sourceUUID
                return if (transactionState.dataList.isEmpty()) {
                    Result.success("Empty dataList")
                } else if (warehouseUUID != null) {
                    return Result.success("warehouse is Live")
                } else {
                    Result.failure(Throwable("Source Warehouse UUID NULL"))
                }
            }
            TransactionType.Transfer -> {
                val sourceWarehouseUUID = transactionState.transactionDetail.sourceUUID
                val targetWarehouseUUID = transactionState.transactionDetail.targetUUID
                return if (transactionState.dataList.isEmpty()) {
                    Result.success("Empty dataList")
                } else if (sourceWarehouseUUID != null && targetWarehouseUUID != null) {
                    transactionState.dataList.forEach { dataDetail->
                        checkStockForDelete(dataDetail.productUUID, targetWarehouseUUID, stringToFloat(dataDetail.piece))
                            .onFailure {
                                return Result.failure(it)
                            }
                    }.let {
                        return Result.success("OK")
                    }
                } else {
                    Result.failure(Throwable("Source and Target Warehouse UUID NULL"))
                }
            }
            else -> {
            return Result.failure(Throwable("TransactionType error"))
            }
        }

    }

    override suspend fun deleteTransaction(transactionState: TransactionState): Result<Boolean> {
        when(transactionState.transactionDetail.transactionType) {
            TransactionType.Arrival -> {
                if(transactionState.dataList.isEmpty()) {
                    deleteTransactionByUUID(transactionState.transactionDetail.uuid)
                    return Result.success(true)
                } else if(transactionState.transactionDetail.targetUUID != null) {
                    val warehouseUUID = transactionState.transactionDetail.targetUUID
                    transactionState.dataList.forEach {transactionDataDetail ->

                    dataSourceRepository.getWarehouseProductStocks(transactionDataDetail.productUUID, warehouseUUID!!)?.let {
                    // STOK BOŞ OLMAMALI ! Neden Ürün eklemişiz. Nereye gitti
                        updateStock(it, transactionDataDetail.piece, StockOperation.REMOVE)
                        dataSourceRepository.deleteTransactionDataByUUID(transactionDataDetail.uuid)
                    }

                    }.let {
                        deleteTransactionByUUID(transactionState.transactionDetail.uuid)
                        return Result.success(true)
                    }

                } else {
                    return Result.failure(Throwable("Arrival targetUUID NULL"))
                }
            }
            TransactionType.Outgoing -> {
                if(transactionState.dataList.isEmpty()) {
                    deleteTransactionByUUID(transactionState.transactionDetail.uuid)
                    return Result.success(true)
                } else if(transactionState.transactionDetail.sourceUUID != null) {

                    val warehouseUUID = transactionState.transactionDetail.sourceUUID
                    transactionState.dataList.forEach {transactionDataDetail ->

                        dataSourceRepository.getWarehouseProductStocks(transactionDataDetail.productUUID, warehouseUUID!!)?.let {
                            // Bu giden ürün, yerine eklemek sorun olmasa gerek
                            // ürün silinmediği sürece boş gelmez
                            updateStock(it, transactionDataDetail.piece, StockOperation.ADD)
                            dataSourceRepository.deleteTransactionDataByUUID(transactionDataDetail.uuid)
                        }

                    }.let {
                        deleteTransactionByUUID(transactionState.transactionDetail.uuid)
                        return Result.success(true)
                    }

                } else {
                    return Result.failure(Throwable("Outgoing sourceUUID NULL"))
                }
            }
            TransactionType.Transfer -> {
                if(transactionState.dataList.isEmpty()) {
                    deleteTransactionByUUID(transactionState.transactionDetail.uuid)
                    return Result.success(true)
                } else if(transactionState.transactionDetail.sourceUUID != null) {
                    val sourceWarehouse = transactionState.transactionDetail.sourceUUID
                    val targetWarehouse = transactionState.transactionDetail.targetUUID
                    transactionState.dataList.forEach {transactionDataDetail ->

                        dataSourceRepository.getWarehouseProductStocks(transactionDataDetail.productUUID, targetWarehouse!!)?.let {
                            // Bu giden ürün, yerine eklemek sorun olmasa gerek
                            // ürün silinmediği sürece boş gelmez
                            updateStock(it, transactionDataDetail.piece, StockOperation.REMOVE)
                        }

                        dataSourceRepository.getWarehouseProductStocks(transactionDataDetail.productUUID, sourceWarehouse!!)?.let {
                            // Bu giden ürün, yerine eklemek sorun olmasa gerek
                            // ürün silinmediği sürece boş gelmez
                            updateStock(it, transactionDataDetail.piece, StockOperation.ADD)
                        }

                        dataSourceRepository.deleteTransactionDataByUUID(transactionDataDetail.uuid)

                    }.let {
                        deleteTransactionByUUID(transactionState.transactionDetail.uuid)
                        return Result.success(true)
                    }

                } else {
                    return Result.failure(Throwable("Outgoing sourceUUID NULL"))
                }
            }
            else -> {
                return Result.failure(Throwable("transactionType Error"))
            }
        }
    }

    override suspend fun deleteAllTransactionsDataByTransactionUUID(transactionUUID: String): Result<Boolean> {
        return dataSourceRepository.deleteAllTransactionsDataByTransactionUUID(transactionUUID)
    }

    override suspend fun deleteTransactionByUUID(uuid: String): Result<Boolean> {
        return dataSourceRepository.deleteTransactionByUUID(uuid)
    }

    override suspend fun deleteTransactionDataByUUID(transactionDataUUID: String): Result<Boolean> {
        return dataSourceRepository.deleteTransactionDataByUUID(transactionDataUUID)
    }
}