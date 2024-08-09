package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandEntity

@Dao
interface BrandDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBrand(brand: BrandEntity)

    @Update
    suspend fun updateBrand(brand: BrandEntity)

    @Delete
    suspend fun deleteBrand(brand: BrandEntity)

    @Query("SELECT * from brands WHERE id = :id")
    fun getBrand(id: Long): BrandEntity?
    @Query("SELECT * from brands WHERE uuid = :uuid")
    fun getBrandUUID(uuid: String): BrandEntity?

    @Query("SELECT * from brands ORDER BY brandName ASC")
    fun getBrands(): List<BrandEntity>

    @Query("DELETE from brands")
    fun deleteALLBrand()
}