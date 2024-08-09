package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.StockEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.StockJoinEntity

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStock(stock: StockEntity)

    @Query("UPDATE stocks SET piece=:newPiece WHERE uuid=:uuid ")
    suspend fun updateStock(newPiece: Float, uuid: String)

    @Delete
    suspend fun deleteStock(stock: StockEntity)

    @Query("SELECT * from stocks WHERE id = :id")
    fun getStock(id: Long): StockEntity?
    @Query("SELECT * from stocks WHERE uuid = :uuid")
    fun getStockUUID(uuid: String): StockEntity?

    @Query("SELECT * from stocks")
    fun getStocks(): List<StockEntity>

    @Query("SELECT * from stocks WHERE warehouseUUID = :warehouseUUID")
    fun getWarehouseStocks(warehouseUUID: String): List<StockEntity>

    @Query("SELECT * from stocks WHERE productUUID = :productUUID")
    fun getProductStocks(productUUID: String): List<StockEntity>

    @Query("SELECT * from stocks WHERE productUUID = :productUUID AND warehouseUUID = :warehouseUUID")
    fun getWarehouseProductStocks(productUUID: String, warehouseUUID: String): StockEntity?

    @Query("DELETE from stocks")
    fun deleteALLStocks()

    @Transaction
    @Query("SELECT * from stocks")
    fun getStockJoin(): List<StockJoinEntity>

}