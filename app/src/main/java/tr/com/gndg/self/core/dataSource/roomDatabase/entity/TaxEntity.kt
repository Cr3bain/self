package tr.com.gndg.self.core.dataSource.roomDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taxes")
data class TaxEntity(
    val uuid: String,
    val name: String,
    val percent : Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}