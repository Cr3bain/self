package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.ProductStocksEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.ProductsJoinEntity
import java.math.BigDecimal

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductEntity)

    @Query("UPDATE products SET name=:name," +
            " description=:description, barcode=:barcode,stockCode=:stockCode , " +
            "minStock=:minStock, avrPurchasePrice=:avrPurchasePrice, " +
            "purchasePrice=:purchasePrice, sellPrice=:sellPrice WHERE uuid=:productUUID")
    suspend fun update(
        productUUID: String,
        name: String,
        description: String?,
        barcode: String?,
        stockCode: String?,
        minStock: BigDecimal?,
        avrPurchasePrice: BigDecimal?,
        purchasePrice: BigDecimal?,
        sellPrice: BigDecimal?)

    @Query("DELETE from products WHERE uuid=:productUUID")
    suspend fun deleteProduct(productUUID: String)

    @Query("SELECT * from products WHERE id = :id")
    fun getProduct(id: Long): ProductEntity?

    @Query("SELECT * from products WHERE uuid = :uuid")
    fun getProductUUID(uuid: String): ProductEntity?

    @Query("SELECT * from products ORDER BY name ASC")
    fun getProducts(): List<ProductEntity>

    @Query("DELETE from products")
    fun deleteALLProducts()

    @Transaction
    @Query("SELECT * from products")
    fun getProductAndStocks(): List<ProductStocksEntity>

    @Transaction
    @Query("SELECT * from products ORDER BY id DESC")
    fun getProductsJoin(): List<ProductsJoinEntity>


    @Transaction
    @Query("SELECT * from products WHERE uuid=:productUUID")
    fun getProductJoin(productUUID: String) : ProductsJoinEntity?

    @Query("UPDATE products SET purchasePrice=:purchasePrice WHERE uuid=:productUUID")
    suspend fun updatePurchasePrice(productUUID: String, purchasePrice: BigDecimal)

    @Query("SELECT * from products WHERE barcode=:barcode")
    suspend fun getProductByBarcode(barcode: String) : ProductEntity?
}