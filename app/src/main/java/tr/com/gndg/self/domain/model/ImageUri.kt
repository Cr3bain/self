package tr.com.gndg.self.domain.model

import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ImageUriEntity

data class ImageUri(
    val id: Long?,
    val uuid: String,
    val productUUID: String,
    val imageUri: String?
)

fun ImageUri.toImageUriEntity() = ImageUriEntity(
    uuid = this.uuid,
    productUUID = this.productUUID,
    imageUri = this.imageUri
)