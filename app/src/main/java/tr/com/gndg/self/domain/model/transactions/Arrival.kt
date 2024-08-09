package tr.com.gndg.self.domain.model.transactions

import tr.com.gndg.self.core.util.TransactionType


data class Arrival(
    override val transactionID: Long,
    override val uuid: String,
    override val transactionType: Int = TransactionType.Arrival,
    override val sourceUUID: String?, //supplier
    override val targetUUID: String = "",
    override val sourceID: Long?,
    override val targetID: Long= 0L,
    override val description: String?,
    override val date: Long,
    override val discountPercent: Float?,
    override val documentNumber: String?,
    override val returned: Boolean,
    override val returnUUID: String?,
    override val expenseID: Long?,
    override val expenseUUID: String?
) : Transaction()
