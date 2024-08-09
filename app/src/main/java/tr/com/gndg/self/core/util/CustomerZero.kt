package tr.com.gndg.self.core.util

import android.content.Context
import org.joda.time.DateTime
import tr.com.gndg.self.R
import tr.com.gndg.self.domain.model.Customer
import tr.com.gndg.self.domain.model.CustomerDetail
import tr.com.gndg.self.domain.model.toCustomer

fun customerZero(context: Context) : Customer {
    return CustomerDetail(
        id = 0,
        uuid = "",
        name = context.getString(R.string.allCustomers),
        createDate = DateTime.now().millis).toCustomer()
}