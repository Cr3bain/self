package tr.com.gndg.self.domain.model

import org.joda.time.DateTime
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SupplierEntity
import java.util.UUID

data class Supplier(
    val id : Long?,
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
    val createDate: Long,
)

data class SupplierDetail(
    val id : Long? = 0L,
    val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val zipCode : String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val website: String? = null,
    val taxNumber: String? = null,
    val description: String? = null,
    val createDate: Long = DateTime.now().millis
)

fun SupplierDetail.toSupplier() = Supplier(
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

fun Supplier.toSupplierDetail() = SupplierDetail(
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

data class SupplierUiState(
    val supplier: Supplier = SupplierDetail().toSupplier()
)

fun Supplier.toSupplierUiState() = SupplierUiState(
    supplier = SupplierDetail(
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
    ).toSupplier()
)

fun Supplier.toSupplierEntity() = SupplierEntity(
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
    createDate = this.createDate,
)
