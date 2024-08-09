package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.LabelDataEntity

@Dao
interface LabelDataDao {
    @Query("SELECT * from labelData")
    fun getAllLabelData(): LabelDataEntity?
}