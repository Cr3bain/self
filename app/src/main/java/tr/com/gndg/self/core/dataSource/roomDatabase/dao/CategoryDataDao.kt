package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.CategoryDataJoinEntity
import tr.com.gndg.self.domain.join.CategoryDataJoin

@Dao
interface CategoryDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(categoryData: CategoryDataEntity)

    @Update
    suspend fun update(categoryData: CategoryDataEntity)

    @Delete
    suspend fun delete(categoryData: CategoryDataEntity)

    @Transaction
    @Query("SELECT * from categoryData WHERE productUUID = :productUUID")
    fun getProductCategoryData(productUUID: String): CategoryDataEntity?

    @Query("DELETE from categoryData  WHERE productUUID = :productUUID")
    fun deleteProductCategoryData(productUUID: String)

    @Transaction
    @Query("SELECT * from categoryData")
    fun getAllCategoryData(): CategoryDataEntity?

    @Transaction
    @Query("SELECT * from categoryData")
    fun getCategoryDataJoin(): CategoryDataJoinEntity?
}