package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.Supplier

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L,
    val uuid: String,
    val name: String,
    val address: String?,
    val city: String?,
    val country: String?,
    val zipCode : String?,
    val phoneNumber: String?,
    val email: String?,
    val website: String?,
    val taxNumber: String?,
    val description: String?,
    val createDate: Long
)

fun SupplierEntity.toSupplier() = Supplier(
    id = this.id,
    uuid = this.uuid,
    name = this.name,
    address = this.address,
    city = this.city,
    country = this.country,
    zipCode = this.zipCode,
    phoneNumber = this.phoneNumber,
    email = this.email,
    website = this.website,
    taxNumber = this.taxNumber,
    description = this.description,
    createDate = this.createDate

)