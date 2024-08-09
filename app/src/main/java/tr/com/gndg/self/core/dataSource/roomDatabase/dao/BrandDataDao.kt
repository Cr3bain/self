package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandDataEntity

@Dao
interface BrandDataDao {

    @Query("SELECT * from brandData")
    fun getAllBrandData(): BrandDataEntity?
}