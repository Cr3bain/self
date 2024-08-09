package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tr.com.gndg.self.domain.model.Label

@Entity(tableName = "labels")
data class LabelEntity(
    val uuid: String,
    val labelName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L
}

fun LabelEntity.toLabel() = Label(
    id = this.id, uuid = this.uuid, labelName = this.labelName
)