package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "expenses",
    indices = [Index(value = arrayOf("id"), unique = true)],)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val uuid: String,
    val expense: BigDecimal,
    val expenseCategoryID: Long?,
    val expenseCategoryUUID: String?
)
