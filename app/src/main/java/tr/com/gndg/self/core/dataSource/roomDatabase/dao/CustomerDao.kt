package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CustomerEntity

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomer(customer: CustomerEntity)


    @Query("UPDATE customers SET name=:name, address=:address, " +
            "city=:city, country=:country, zipCode=:zipCode, phoneNumber=:phoneNumber, " +
            "email=:email, website=:website, taxNumber=:taxNumber, description=:description WHERE uuid=:customerUUID")
    suspend fun updateCustomer(
        customerUUID: String,
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

    @Query("DELETE from customers WHERE uuid=:customerUUID")
    suspend fun deleteCustomer(customerUUID: String)

    @Query("SELECT * from customers WHERE id = :id")
    fun getCustomer(id: Long): CustomerEntity?

    @Query("SELECT * from customers WHERE uuid = :uuid")
    fun getCustomerByUUID(uuid: String): CustomerEntity?

    @Query("SELECT * from customers ORDER BY name ASC")
    fun getCustomersByName(): List<CustomerEntity>

    @Query("SELECT * from customers")
    fun getCustomers(): List<CustomerEntity>

    @Query("DELETE from customers")
    fun deleteALLProducts()
}