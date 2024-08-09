package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.ProductTransactionEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.TransactionJoinDataListEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionEntity

@Dao
interface TransactionDao {

    @Query("SELECT * from transactions")
    fun getTransactions(): List<TransactionEntity>

    @Query("SELECT transactionID from transactions ORDER BY transactionID DESC LIMIT 1")
    suspend fun getLastTransactionID(): Long?

    @Transaction
    @Query("SELECT * from transactions WHERE transactionID=:transactionID")
    suspend fun getTransactionByID(transactionID: Long): TransactionJoinDataListEntity?

    @Transaction
    @Query("SELECT * from transactions WHERE uuid=:transactionUUID")
    suspend fun getTransactionByUUID(transactionUUID: String): TransactionJoinDataListEntity?

    @Transaction
    @Query("SELECT * from transactions")
    suspend fun getTransactionJoinDataList() : List<TransactionJoinDataListEntity>

    @Query("SELECT * from transactionData")
    fun getTransactionData(): List<TransactionDataEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransactionData(transactionData: TransactionDataEntity)

    @Transaction
    @Query("SELECT * from transactionData WHERE productUUID=:productUUID")
    suspend fun getProductTransactions(productUUID: String) : List<ProductTransactionEntity>

    @Query("DELETE from transactionData WHERE uuid=:transactionDataUUID")
    suspend fun deleteTransactionDataByUUID(transactionDataUUID : String)

    @Query("DELETE from transactions WHERE uuid=:uuid")
    suspend fun deleteTransactionByUUID(uuid : String)

    @Query("DELETE from transactionData WHERE transactionUUID=:transactionUUID")
    suspend fun deleteAllTransactionsDataByTransactionUUID(transactionUUID : String)


    @Query("UPDATE transactions SET sourceUUID=:sourceUUID," +
            " targetUUID=:targetUUID, sourceID=:sourceID, " +
            "targetID=:targetID, description=:description, " +
            "date=:date, discountPercent=:discountPercent, documentNumber=:documentNumber WHERE transactionID=:transactionID")
    suspend fun updateTransactionEntity(
        transactionID: Long,
        sourceUUID: String?,
        targetUUID: String?,
        sourceID: Long?,
        targetID: Long?,
        description: String?,
        date: Long?,
        discountPercent: Float?,
        documentNumber: String?)

    @Query(" SELECT (SELECT COUNT(*) from transactions WHERE sourceUUID=:warehouseUUId OR targetUUID=:warehouseUUId) == 0 ")
    suspend fun warehouseDeleteRequest(warehouseUUId: String) : Boolean

    @Transaction
    @Query("SELECT * from transactions WHERE transactionType=:transactionType")
    suspend fun getTransactionByType(transactionType : Int) : List<TransactionJoinDataListEntity>

    @Transaction
    @Query("SELECT * from transactions WHERE transactionType=:transactionType AND sourceID=:id")
    suspend fun getArrivalByTypeAndId(transactionType : Int, id: Long) : List<TransactionJoinDataListEntity>

    @Transaction
    @Query("SELECT * from transactions WHERE transactionType=:transactionType AND targetID=:id")
    suspend fun getOutgoingByTypeAndId(transactionType : Int, id: Long) : List<TransactionJoinDataListEntity>
}