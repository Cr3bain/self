package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.ImageUri

@Entity(tableName = "imageUris")
data class ImageUriEntity(
    val uuid: String,
    val productUUID: String,
    val imageUri: String?
){
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L
}

fun ImageUriEntity.toImageUri() = ImageUri(
    id = this.id,
    uuid = this.uuid,
    productUUID = this.productUUID,
    imageUri = this.imageUri
)