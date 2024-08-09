package tr.com.gndg.self.core.dataSource.roomDatabase.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toCategory
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toCategoryData
import tr.com.gndg.self.domain.join.CategoryJoin

data class CategoryJoinEntity(
    @Embedded
    val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "categoryUUID"
    )
    val categoryDataEntity: List<CategoryDataEntity>,

    )

fun CategoryJoinEntity.toCategoryJoin() = CategoryJoin(
    this.categoryEntity.toCategory(),
    this.categoryDataEntity.map { it.toCategoryData() }
)