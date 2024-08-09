package tr.com.gndg.self.core.dataSource.roomDatabase.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.toTransactionData
import tr.com.gndg.self.domain.join.ProductTransaction

data class ProductTransactionEntity(
    @Embedded
    val transactionDataEntity: TransactionDataEntity?,
    @Relation(
        parentColumn = "transactionUUID",
        entityColumn = "uuid",
        entity = TransactionEntity::class)
    val transactionEntity: TransactionEntity?
)

fun ProductTransactionEntity.toProductTransaction() = ProductTransaction(
    this.transactionDataEntity?.toTransactionData(),
    this.transactionEntity
)