package tr.com.gndg.self.core.dataSource.roomDatabase.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.toTransactionData
import tr.com.gndg.self.domain.join.TransactionJoinData
import tr.com.gndg.self.domain.model.transactions.toTransactionEntity

data class TransactionJoinDataListEntity(
    @Embedded
    val transaction: TransactionEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "transactionUUID",
        entity = TransactionDataEntity::class)
    val dataList: List<TransactionDataEntity>
)

fun TransactionJoinDataListEntity.toTransactionJoinData() = TransactionJoinData(
    transaction = this.transaction.toTransactionEntity(),
    dataList = this.dataList.map { it.toTransactionData() }
)