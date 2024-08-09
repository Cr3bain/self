package tr.com.gndg.self.domain.model.transactions

import androidx.compose.runtime.mutableStateListOf
import org.joda.time.DateTime
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionEntity
import java.util.UUID

abstract class Transaction {
    abstract val transactionID:Long
    abstract val uuid: String
    abstract val transactionType: Int
    abstract val sourceUUID: String?
    abstract val targetUUID: String?
    abstract val sourceID: Long?
    abstract val targetID: Long?
    abstract val description: String?
    abstract val date : Long
    abstract val discountPercent : Float?
    abstract val documentNumber : String?
    abstract val returned: Boolean
    abstract val returnUUID: String?
    abstract val expenseID: Long?
    abstract val expenseUUID: String?
}

data class TransactionDetail(
    val transactionID : Long =0,
    val uuid: String = "",
    val transactionType : Int = 0,
    val sourceUUID: String? = null,
    var targetUUID: String? = null,
    val sourceID: Long? = null,
    var targetID: Long? = null,
    val description: String? = null,
    val date: Long = DateTime.now().millis,
    val discountPercent: Float? = null,
    val documentNumber: String? = null,
    val returned : Boolean = false,
    val returnUUID: String? = null,
    val expenseID: Long? = null,
    val expenseUUID: String? = null
)

fun TransactionDetail.toArrival() = Arrival(
    transactionID = this.transactionID,
    uuid = this.uuid,
    transactionType = this.transactionType,
    sourceUUID = this.sourceUUID,
    targetUUID = this.targetUUID?:"",
    sourceID = this.sourceID,
    targetID = this.targetID?:0,
    description = this.description,
    date = this.date,
    discountPercent = this.discountPercent,
    documentNumber = this.documentNumber,
    returned= this.returned,
    returnUUID = this.returnUUID,
    expenseID = expenseID,
    expenseUUID = expenseUUID
)

fun TransactionDetail.toOutgoing() = OutGoing(
    transactionID = this.transactionID,
    uuid = this.uuid,
    transactionType = this.transactionType,
    sourceUUID = this.sourceUUID?:"",
    targetUUID = this.targetUUID,
    sourceID = this.sourceID?:0,
    targetID = this.targetID,
    description = this.description,
    date = this.date,
    discountPercent = this.discountPercent,
    documentNumber = this.documentNumber,
    returned= this.returned,
    returnUUID = this.returnUUID,
    expenseID = expenseID,
    expenseUUID = expenseUUID

)

fun TransactionDetail.toTransfer() = Transfer(
    transactionID = this.transactionID,
    uuid = this.uuid,
    transactionType = this.transactionType,
    sourceUUID = this.sourceUUID?:"",
    targetUUID = this.targetUUID?:"",
    sourceID = this.sourceID?:0L,
    targetID = this.targetID?:0L,
    description = this.description,
    date = this.date,
    discountPercent = this.discountPercent,
    documentNumber = this.documentNumber,
    returned= this.returned,
    returnUUID = this.returnUUID,
    expenseID = expenseID,
    expenseUUID = expenseUUID
)


data class TransactionState(
    val uuid: String = UUID.randomUUID().toString(),
    var transactionDetail: TransactionDetail = TransactionDetail(
        uuid = uuid,
        transactionType = 0
    ),
    var dataList : MutableList<TransactionDataDetail> = mutableStateListOf()
)

fun Transaction.toTransactionEntity() = TransactionEntity(
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
    returnUUID = this.returnUUID,
    expenseID = expenseID,
    expenseUUID = expenseUUID
)

fun Transaction.toTransactionDetail() = TransactionDetail(
    transactionID = this.transactionID,
    uuid = this.uuid,
    transactionType = this.transactionType,
    sourceUUID = this.sourceUUID,
    targetUUID = this.targetUUID?:"",
    sourceID = this.sourceID,
    targetID = this.targetID?:0L,
    description = this.description,
    date = this.date,
    discountPercent = this.discountPercent,
    documentNumber = this.documentNumber,
    returned= this.returned,
    returnUUID = this.returnUUID,
    expenseID = expenseID,
    expenseUUID = expenseUUID
)