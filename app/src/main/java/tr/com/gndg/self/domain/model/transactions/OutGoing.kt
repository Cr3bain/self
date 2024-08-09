package tr.com.gndg.self.domain.model.transactions

import tr.com.gndg.self.core.util.TransactionType


data class OutGoing(
    override val transactionID: Long,
    override val uuid: String,
    override val transactionType: Int = TransactionType.Outgoing,
    override val sourceUUID: String = "", //warehouse
    override val targetUUID: String?, // customer
    override val sourceID: Long = 0L, //warehouse
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
