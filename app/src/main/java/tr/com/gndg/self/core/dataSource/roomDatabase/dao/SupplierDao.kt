package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SupplierEntity

@Dao
interface SupplierDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSupplier(supplier: SupplierEntity)

    @Query("UPDATE suppliers SET name=:name, address=:address, " +
            "city=:city, country=:country, zipCode=:zipCode, phoneNumber=:phoneNumber, " +
            "email=:email, website=:website, taxNumber=:taxNumber, description=:description WHERE uuid=:supplierUUID")
    suspend fun updateSupplier(
        supplierUUID: String,
        name: String,
        address: String?,
        city: String?,
        country: String?,
        zipCode: String?,
        phoneNumber: String?,
        email: String?,
        website: String?,
        taxNumber: String?,
        description: String?

    )

    @Query("DELETE from suppliers WHERE uuid=:supplierUUID")
    suspend fun deleteSupplier(supplierUUID: String)

    @Query("SELECT * from suppliers WHERE id = :id")
    fun getSupplier(id: Long): SupplierEntity?

    @Query("SELECT * from suppliers WHERE uuid = :uuid")
    fun getSupplierUUID(uuid: String): SupplierEntity?

    @Query("SELECT * from suppliers ORDER BY name ASC")
    fun getSupplierByName(): List<SupplierEntity>

    @Query("SELECT * from suppliers")
    fun getSuppliers(): List<SupplierEntity>

    @Query("DELETE from suppliers")
    fun deleteALLProducts()
}