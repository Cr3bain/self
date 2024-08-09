package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryDataEntity

data class CategoryData(
    val id : Long?,
    val uuid: String,
    val categoryUUID: String,
    val productUUID: String,
    val categoryDataName: String?,
    val categoryID: Long
)

fun CategoryData.toCategoryEntity() = CategoryDataEntity(
    uuid = this.uuid,
    categoryUUID = this.categoryUUID,
    productUUID = this.productUUID,
    categoryDataName = this.categoryDataName,
    categoryID = categoryID

)