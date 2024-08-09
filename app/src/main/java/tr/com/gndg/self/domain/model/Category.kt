package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryEntity

data class Category(
    val id: Long?,
    val uuid: String,
    val categoryName: String
)

fun Category.toCategoryEntity() = CategoryEntity(
    uuid = this.uuid,
    categoryName = this.categoryName
)
