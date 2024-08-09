package tr.com.gndg.self.domain.join

import tr.com.gndg.self.domain.model.transactions.Transaction
import tr.com.gndg.self.domain.model.transactions.TransactionData
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionState
import tr.com.gndg.self.domain.model.transactions.toTransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.toTransactionDetail

data class TransactionJoinData(
    val transaction: Transaction,
    val dataList: List<TransactionData>?
)

fun TransactionJoinData.toTransactionState() = TransactionState(
    uuid = this.transaction.uuid,
    transactionDetail = this.transaction.toTransactionDetail(),
    dataList = this.dataList?.map { it.toTransactionDataDetail() }?.toMutableList()?: emptyList<TransactionDataDetail>().toMutableList()
)
