package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyEntity(
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
){
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L
}