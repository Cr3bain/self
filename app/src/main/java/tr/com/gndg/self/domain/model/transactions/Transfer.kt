package tr.com.gndg.self.domain.model.transactions

import tr.com.gndg.self.core.util.TransactionType

data class Transfer(
    override val transactionID: Long,
    override val uuid: String,
    override val transactionType: Int = TransactionType.Transfer,
    override val sourceUUID: String = "", // sourceWarehouse
    override val targetUUID: String = "", // targetWarehouse
    override val sourceID: Long =0L,
    override val targetID: Long = 0L,
    override val description: String?,
    override val date: Long,
    override val discountPercent: Float?,
    override val documentNumber: String?,
    override val returned: Boolean,
    override val returnUUID: String?,
    override val expenseID: Long?,
    override val expenseUUID: String?

) : Transaction()

/*

fun Transfer.toTransactionEntity() = TransactionEntity(
    transactionID = this.transactionID,
    uuid = this.uuid,
    transactionType = this.transactionType,
    sourceUUID = this.sourceUUID,
    targetUUID = this.targetUUID,
    sourceID = this.sourceID,
    targetID = this.targetID,
    description = this.description,
    date = this.date,
    discountPercent = this.discountPercent,
    documentNumber = this.documentNumber,
    returned = this.returned,
    returnUUID = this.returnUUID
)*/
