package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SizeEntity

@Dao
interface SizeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSize(size: SizeEntity)

    @Update
    suspend fun updateSize(size: SizeEntity)

    @Delete
    suspend fun deleteSize(size: SizeEntity)

    @Query("SELECT * from sizes WHERE id = :id")
    fun getSize(id: Long): SizeEntity?
    @Query("SELECT * from sizes WHERE uuid = :uuid")
    fun getSizeUUID(uuid: String): SizeEntity?

    @Query("SELECT * from sizes ORDER BY sizeName ASC")
    fun getSizes(): List<SizeEntity>

    @Query("DELETE from sizes")
    fun deleteALLSizes()
}