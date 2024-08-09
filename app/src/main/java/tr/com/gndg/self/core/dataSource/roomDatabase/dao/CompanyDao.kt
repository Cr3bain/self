package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CompanyEntity

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(company: CompanyEntity)

    @Update
    suspend fun update(company: CompanyEntity)

    @Delete
    suspend fun delete(company: CompanyEntity)

    @Query("SELECT * from companies WHERE id = :id")
    fun getBrand(id: Long): CompanyEntity?
    @Query("SELECT * from companies WHERE uuid = :uuid")
    fun getBrandUUID(uuid: String): CompanyEntity?

    @Query("SELECT * from companies ORDER BY name ASC")
    fun getProducts(): List<CompanyEntity>

    @Query("DELETE from companies")
    fun deleteALLProducts()
}