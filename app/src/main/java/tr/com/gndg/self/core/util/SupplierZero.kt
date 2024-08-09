package tr.com.gndg.self.core.util

import android.content.Context
import org.joda.time.DateTime
import tr.com.gndg.self.R
import tr.com.gndg.self.domain.model.Supplier
import tr.com.gndg.self.domain.model.SupplierDetail
import tr.com.gndg.self.domain.model.toSupplier

fun supplierZero(context: Context) : Supplier {
    return SupplierDetail(
        id = 0,
        uuid = "",
        name = context.getString(R.string.allSuppliers),
        createDate = DateTime.now().millis).toSupplier()

}