package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CompanyEntity

data class Company(
    val id : Long?,
    val uuid: String,
    val name: String,
    val address: String?,
    val city: String?,
    val country: String?,
    val zipCode : String?,
    val phoneNumber: Int?,
    val email: String?,
    val website: String?,
    val taxNumber: String?,
    val discount: Float?,
    val description: String?,
)

fun Company.toCompanyEntity() = CompanyEntity(
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
    description = this.description
)
