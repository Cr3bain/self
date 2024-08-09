package tr.com.gndg.self.core.dataSource.roomDatabase.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toCategoryData
import tr.com.gndg.self.domain.join.CategoryDataJoin

data class CategoryDataJoinEntity(
    @Embedded
    val categoryDataEntity: CategoryDataEntity,
    @Relation(
        //parentColumn = "categoryUUID",
        //entityColumn = "uuid",
        parentColumn = "categoryID",
        entityColumn = "id",
        entity = CategoryEntity::class,
        projection = ["categoryName"]
    )
    val categoryDataName: String?
)

fun CategoryDataJoinEntity.toCategoryDataJoin() = CategoryDataJoin(
    this.categoryDataEntity.toCategoryData(),
    this.categoryDataName
)