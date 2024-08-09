package tr.com.gndg.self.core.dataSource.roomDatabase.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ImageUriEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.LabelDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SizeDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.StockEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toProduct
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toStock
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionDataEntity
import tr.com.gndg.self.domain.join.ProductsJoin


data class ProductsJoinEntity(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID")
    val stock: List<StockEntity>,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID",
        entity = TransactionDataEntity::class,)
    val productTransactions: List<ProductTransactionEntity>,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID",
        entity = CategoryDataEntity::class,
        projection = ["categoryDataName"]
    )
    val categoryDataName: String?,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID",
        entity = ImageUriEntity::class,
        projection = ["imageUri"]
    )
    val imageUri : String?,

/*    @Ignore
    val taxPercent: Float?,*/

    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID",
        entity = BrandDataEntity::class,
        projection = ["brandDataName"]
    )
    val brandDataName: String?,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID",
        entity = SizeDataEntity::class,
        projection = ["sizeDataName"]
    )
    val sizeDataName : String?,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "productUUID",
        entity = LabelDataEntity::class,
        projection = ["labelDataName"]
    )
    val labelDataName: String?
    )

fun ProductsJoinEntity.toProductsJoin() = ProductsJoin(
    product = this.product.toProduct(),
    stock = this.stock.map { it.toStock() },
    productTransactions= this.productTransactions.map { it.toProductTransaction() },
    categoryName = this.categoryDataName,
    imageUri = this.imageUri,
    brandDataName = this.brandDataName,
    sizeDataName = this.sizeDataName,
    labelDataName = this.labelDataName
)