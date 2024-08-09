package tr.com.gndg.self.domain.model.transactions

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionDataEntity
import tr.com.gndg.self.core.util.bigDecimalToString
import tr.com.gndg.self.core.util.floatToString
import tr.com.gndg.self.core.util.stringToBigDecimal
import java.math.BigDecimal
import java.util.UUID


data class TransactionData(
    var id : Long,
    val uuid: String,
    val transactionUUID: String,
    val productUUID: String,
    val productID: Long,
    val piece : Float,
    val unitPrice: BigDecimal?,
    val profit: BigDecimal?
)

data class TransactionDataDetail(
    val uuid: String = UUID.randomUUID().toString(),
    val transactionUUID: String,
    var productUUID: String = "",
    var productID: Long = 0L,
    var piece : String = "1",
    var unitPrice: String = "",
    val profit: BigDecimal? = null
)

fun TransactionData.toTransactionDataDetail() = TransactionDataDetail(
    uuid = this.uuid,
    transactionUUID = this.transactionUUID,
    productUUID = this.productUUID,
    productID = this.productID,
    piece = floatToString(this.piece),
    unitPrice = bigDecimalToString(this.unitPrice),
    profit = this.profit

)

fun TransactionDataDetail.toTransactionData() = TransactionData(
    id =0,
    uuid = this.uuid,
    transactionUUID = this.transactionUUID,
    productUUID = this.productUUID,
    productID = this.productID,
    piece = this.piece.toFloat(),
    unitPrice = stringToBigDecimal(this.unitPrice),
    profit = this.profit
)

fun TransactionData.toTransactionDataEntity() = TransactionDataEntity(
    id = this.id,
    uuid = this.uuid,
    transactionUUID = this.transactionUUID,
    productUUID = this.productUUID,
    productID = this.productID,
    piece = this.piece,
    unitPrice = this.unitPrice,
    profit = this.profit
)
