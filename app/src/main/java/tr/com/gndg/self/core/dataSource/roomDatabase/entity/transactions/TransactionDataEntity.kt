package tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductEntity
import tr.com.gndg.self.domain.model.transactions.TransactionData
import java.math.BigDecimal

@Entity(tableName = "transactionData",
    indices = [Index(value = arrayOf("id"), unique = true), Index("productID"),Index("transactionUUID"), Index(value = arrayOf("uuid"), unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productID"],
            /* Optional (helps maintain referential integrity) */
            /* if parent is deleted then children rows of that parent are deleted */
            onDelete = ForeignKey.CASCADE,
            /* if parent column is changed then the column that references the parent is changed to the same value */
            onUpdate = ForeignKey.CASCADE
        )
    ])
data class TransactionDataEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L,
    val uuid: String,
    val transactionUUID: String,
    val productUUID: String,
    val productID: Long,
    val piece : Float,
    val unitPrice: BigDecimal?,
    val profit: BigDecimal?
)

fun TransactionDataEntity.toTransactionData() = TransactionData(
    id = this.id,
    uuid = this.uuid,
    transactionUUID = this.transactionUUID,
    productUUID = this.productUUID,
    productID = this.productID,
    piece = this.piece,
    unitPrice = this.unitPrice,
    profit = this.profit
)
