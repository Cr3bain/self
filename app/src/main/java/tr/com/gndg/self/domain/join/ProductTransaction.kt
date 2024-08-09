package tr.com.gndg.self.domain.join

import tr.com.gndg.self.domain.model.transactions.Transaction
import tr.com.gndg.self.domain.model.transactions.TransactionData

data class ProductTransaction(
    val transactionData: TransactionData?,
    val transaction: Transaction?
)
