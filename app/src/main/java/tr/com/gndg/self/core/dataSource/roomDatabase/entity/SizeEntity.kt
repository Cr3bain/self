package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.Size

@Entity(tableName = "sizes")
data class SizeEntity(
    val uuid: String,
    val sizeName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}

fun SizeEntity.toSize() = Size(
    id = id, uuid = uuid, sizeName = sizeName
)