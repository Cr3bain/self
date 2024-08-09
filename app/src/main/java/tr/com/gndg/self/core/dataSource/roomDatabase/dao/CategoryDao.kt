package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.CategoryJoinEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * from categories WHERE id = :id")
    fun getCategory(id: Long): CategoryEntity?
    @Query("SELECT * from categories WHERE uuid = :uuid")
    fun getCategoryUUID(uuid: String): CategoryEntity?

    @Query("SELECT * from categories ORDER BY categoryName ASC")
    fun getCategories(): List<CategoryEntity>

    @Query("DELETE from categories")
    fun deleteALLProducts()

    @Transaction
    @Query("SELECT * from categories")
    fun getCategoryJoin(): List<CategoryJoinEntity>
}