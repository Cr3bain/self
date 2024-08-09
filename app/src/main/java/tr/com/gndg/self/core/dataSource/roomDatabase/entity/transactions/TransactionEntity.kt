package tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.transactions.Transaction

@Entity(tableName = "transactions",
    indices = [Index(value = arrayOf("transactionID"), unique = true)],
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    override val transactionID: Long,
    override val uuid: String,
    override val transactionType: Int,
    override val sourceUUID: String?,
    override val targetUUID: String?,
    override val sourceID: Long?,
    override val targetID: Long?,
    override val description: String?,
    override val date: Long,
    override val discountPercent: Float?,
    override val documentNumber: String?,
    override val returned: Boolean,
    override val returnUUID: String?,
    override val expenseID: Long?,
    override val expenseUUID: String?
) : Transaction()
