package tr.com.gndg.self.domain.join

import tr.com.gndg.self.domain.model.Category
import tr.com.gndg.self.domain.model.CategoryData

data class CategoryJoin(
    val category: Category,
    val categoryData: List<CategoryData>
)
