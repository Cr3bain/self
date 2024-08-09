package tr.com.gndg.self.core.util

import android.content.Context
import tr.com.gndg.self.R
import tr.com.gndg.self.domain.model.Warehouse

fun warehouseZero(context: Context) : Warehouse {
    return Warehouse(
        id = 0,
        uuid = "",
        name = context.getString(R.string.allWarehouses),
        "")
}
