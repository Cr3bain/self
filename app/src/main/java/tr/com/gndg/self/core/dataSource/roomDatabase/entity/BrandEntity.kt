package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brands")
data class BrandEntity(
    val uuid: String,
    val brandName: String
){
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L
}