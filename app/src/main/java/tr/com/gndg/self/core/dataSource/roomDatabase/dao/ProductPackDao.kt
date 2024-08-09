package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductPackEntity

@Dao
interface ProductPackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productPack: ProductPackEntity)

    @Update
    suspend fun update(productPack: ProductPackEntity)

    @Delete
    suspend fun delete(productPack: ProductPackEntity)

    @Query("SELECT * from productPacks WHERE id = :id")
    fun getBrand(id: Long): ProductPackEntity?
    @Query("SELECT * from productPacks WHERE uuid = :uuid")
    fun getBrandUUID(uuid: String): ProductPackEntity?

    @Query("SELECT * from productPacks ORDER BY name ASC")
    fun getProducts(): List<ProductPackEntity>

    @Query("DELETE from productPacks")
    fun deleteALLProducts()
}