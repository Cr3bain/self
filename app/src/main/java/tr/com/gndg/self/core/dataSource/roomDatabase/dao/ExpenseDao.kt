package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ExpenseEntity

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: ExpenseEntity)
}