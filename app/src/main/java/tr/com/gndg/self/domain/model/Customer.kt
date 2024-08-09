package tr.com.gndg.self.domain.model

import org.joda.time.DateTime
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CustomerEntity
import java.util.UUID

data class Customer(
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
    val discount: Float?,
    val description: String?,
    val createDate: Long,
)

data class CustomerDetail(
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
    val discount: Float? = null,
    val description: String? = null,
    val createDate: Long = DateTime.now().millis
)

fun CustomerDetail.toCustomer() = Customer(
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
    discount = this.discount,
    description = this.description,
    createDate = this.createDate,
)

fun Customer.toCustomerDetail() = CustomerDetail(
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
    discount = this.discount,
    description = this.description,
    createDate = this.createDate
)

data class CustomerUiState(
    val customer: Customer = CustomerDetail().toCustomer()
)

fun Customer.toCustomerUiState() = CustomerUiState(
    customer = CustomerDetail(
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
        discount = this.discount,
        description = this.description,
        createDate = this.createDate
    ).toCustomer()
)

fun Customer.toCustomerEntity() = CustomerEntity(
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
    discount = this.discount,
    description = this.description,
    createDate = this.createDate
)
