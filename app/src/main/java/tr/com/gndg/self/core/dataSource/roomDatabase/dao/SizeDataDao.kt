package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SizeDataEntity

@Dao
interface SizeDataDao {

    @Query("SELECT * from sizeData")
    fun getSizeData(): SizeDataEntity?
}