package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.TaxEntity

@Dao
interface TaxDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tax: TaxEntity)

    @Update
    suspend fun update(tax: TaxEntity)

    @Delete
    suspend fun delete(tax: TaxEntity)

    @Query("SELECT * from taxes WHERE id = :id")
    fun getBrand(id: Long): TaxEntity?
    @Query("SELECT * from taxes WHERE uuid = :uuid")
    fun getBrandUUID(uuid: String): TaxEntity?

    @Query("SELECT * from taxes ORDER BY name ASC")
    fun getProducts(): List<TaxEntity>

    @Query("DELETE from taxes")
    fun deleteALLProducts()
}