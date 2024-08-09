package tr.com.gndg.self.ui.home.domain

import android.content.Context
import androidx.compose.ui.Modifier
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.ui.animations.MenuAnimation
import tr.com.gndg.self.ui.backup.BackupDestination
import tr.com.gndg.self.ui.customers.CustomersDestination
import tr.com.gndg.self.ui.home.domain.model.HomeMenuItem
import tr.com.gndg.self.ui.inventory.InventoryDestination
import tr.com.gndg.self.ui.reports.ReportsDestination
import tr.com.gndg.self.ui.suppliers.SuppliersDestination
import tr.com.gndg.self.ui.transactions.TransactionsDestination
import tr.com.gndg.self.ui.transactions.newTransaction.TransactionPortalDestination
import tr.com.gndg.self.ui.warehouses.WarehousesDestination


fun homeMenuList(context: Context) : List<HomeMenuItem> {
    return listOf(

        HomeMenuItem(
            name = context.getString(R.string.warehouseScreen_title).uppercase(),
            route = WarehousesDestination.routeWithArgs,
            key = null,
            icon = R.drawable.warehouse_48,
            iconContentDescription =  context.getString(R.string.warehouseScreen_title)),

        HomeMenuItem(
            name = context.getString(R.string.inventoryScreen_title).uppercase(),
            route = InventoryDestination.route ,
            key = 1,
            icon = R.drawable.stacking_48,
            iconContentDescription =  context.getString(R.string.inventoryScreen_title),
            composable = { MenuAnimation(modifier = Modifier, menuName = context.getString(R.string.inventoryScreen_title).uppercase()) }
        ),

        HomeMenuItem(
            name = context.getString(R.string.reportsScreen_title).uppercase(),
            route = ReportsDestination.route,
            key = null,
            icon = R.drawable.increase_48,
            iconContentDescription =  context.getString(R.string.reportsScreen_title),),

        HomeMenuItem(
            name = context.getString(R.string.transactionsScreen_title).uppercase(),
            route = TransactionsDestination.route,
            key = null,
            icon = R.drawable.transaction_list_48,
            iconContentDescription =  context.getString(R.string.transactionsScreen_title)),

        HomeMenuItem(
            name = context.getString(R.string.suppliersScreen_title).uppercase(),
            route = SuppliersDestination.route,
            key = null,
            icon = R.drawable.supplier_48,
            iconContentDescription =  context.getString(R.string.suppliersScreen_title)),

        HomeMenuItem(
            name = context.getString(R.string.customersScreen_title).uppercase(),
            route = CustomersDestination.route,
            key = null,
            icon = R.drawable.customer_48,
            iconContentDescription =  context.getString(R.string.customersScreen_title)),
    )
}

fun homeMenuTripleList(context: Context) : List<HomeMenuItem> {
    return listOf(
        HomeMenuItem(
            name = context.getString(R.string.transfer).uppercase(),
            route = TransactionPortalDestination.route+"/${TransactionType.Transfer}",
            key = null,
            icon = R.drawable.move_stock_48,
            iconContentDescription =  context.getString(R.string.transfer)),

        HomeMenuItem(
            name = context.getString(R.string.outgoing).uppercase(),
            route = TransactionPortalDestination.route+"/${TransactionType.Outgoing}",
            key = null,
            icon = R.drawable.packing_48,
            iconContentDescription =  context.getString(R.string.outgoing)),

        HomeMenuItem(
            name = context.getString(R.string.arrival).uppercase(),
            route = TransactionPortalDestination.route+"/${TransactionType.Arrival}",
            key = null,
            icon = R.drawable.unpacking_48,
            iconContentDescription =  context.getString(R.string.arrival)),

    )
}

fun homeMenuIconList(context: Context) : List<HomeMenuItem> {
    return listOf(
        HomeMenuItem(
            name = context.getString(R.string.backup).uppercase(),
            route = BackupDestination.route,
            key = null,
            icon = R.drawable.baseline_backup_24,
            iconContentDescription =  context.getString(R.string.backup)),

        )
}