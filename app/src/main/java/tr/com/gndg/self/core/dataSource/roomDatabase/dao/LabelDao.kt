package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.LabelEntity

@Dao
interface LabelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(label: LabelEntity)

    @Update
    suspend fun updateLabel(label: LabelEntity)

    @Delete
    suspend fun deleteLabel(label: LabelEntity)

    @Query("SELECT * from labels WHERE id = :id")
    fun getLabel(id: Long): LabelEntity?

    @Query("SELECT * from labels WHERE uuid = :uuid")
    fun getLabelUUID(uuid: String): LabelEntity?

    @Query("SELECT * from labels ORDER BY labelName ASC")
    fun getLabels(): List<LabelEntity>

    @Query("DELETE from labels")
    fun deleteALLLabels()
}