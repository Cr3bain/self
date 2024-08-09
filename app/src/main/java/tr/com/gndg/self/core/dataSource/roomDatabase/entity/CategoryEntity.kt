package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    val uuid: String,
    val categoryName: String
){
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}

fun CategoryEntity.toCategory() = Category(
    id = this.id,
    uuid = this.uuid,
    categoryName = this.categoryName
)