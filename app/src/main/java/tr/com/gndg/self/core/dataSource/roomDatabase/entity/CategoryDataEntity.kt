package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.CategoryData


@Entity(tableName = "categoryData",
    indices = [Index(value = arrayOf("id"), unique = true), Index("categoryID")],
    foreignKeys = [
        /* A MovieId MUST be a value of an existing id column in the movie table */
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryID"],
            /* Optional (helps maintain referential integrity) */
            /* if parent is deleted then children rows of that parent are deleted */
            onDelete = ForeignKey.CASCADE,
            /* if parent column is changed then the column that references the parent is changed to the same value */
            onUpdate = ForeignKey.CASCADE
        )
    ])
data class CategoryDataEntity(
    val uuid: String,
    val categoryUUID: String,
    val productUUID: String,
    val categoryDataName: String?,
    val categoryID: Long,

) {
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L
}

fun CategoryDataEntity.toCategoryData() = CategoryData(
    id = this.id,
    uuid = this.uuid,
    categoryUUID = this.categoryUUID,
    productUUID = this.productUUID,
    categoryDataName = this.categoryDataName,
    categoryID = this.categoryID
)