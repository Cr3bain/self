package tr.com.gndg.self.core.dataSource.roomDatabase.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.math.BigDecimal

class LocalTypeConverter {

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String {
        return input?.toPlainString() ?: ""
    }

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        if (input.isNullOrBlank()) return BigDecimal.valueOf(0.0)
        return input.toBigDecimalOrNull() ?: BigDecimal.valueOf(0.0)
    }
/*

    @TypeConverter
    fun listToJson(value: List<JobWorkHistory>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<JobWorkHistory>::class.java).toList()
*/

}