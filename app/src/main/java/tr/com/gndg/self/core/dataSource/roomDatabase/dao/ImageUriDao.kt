package tr.com.gndg.self.core.dataSource.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ImageUriEntity

@Dao
interface ImageUriDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImageUri(imageUri: ImageUriEntity)

    @Query("UPDATE imageUris SET imageUri=:imageUri WHERE productUUID=:productUUID")
    suspend fun updateImageUri(productUUID: String, imageUri: String?)

    @Query("DELETE from imageUris WHERE productUUID=:productUUID AND imageUri=:imageUri")
    suspend fun deleteImageUri(productUUID: String, imageUri: String?)

    @Query("SELECT * from imageUris WHERE id = :id")
    fun getImageUri(id: Long): ImageUriEntity?

    @Query("SELECT * from imageUris WHERE uuid = :uuid")
    fun getImageUriUUID(uuid: String): ImageUriEntity?

    @Query("SELECT * from imageUris WHERE productUUID = :productUUID")
    fun getProductImageUriUUID(productUUID: String): ImageUriEntity?

    @Query("SELECT * from imageUris")
    fun getImageUris(): List<ImageUriEntity>

    @Query("DELETE from imageUris")
    fun deleteALLImageUris()
}