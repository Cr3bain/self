package tr.com.gndg.self.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.getViewModel
import tr.com.gndg.self.core.camera.CameraDestination
import tr.com.gndg.self.core.camera.CameraOpen
import tr.com.gndg.self.ui.backup.BackupDestination
import tr.com.gndg.self.ui.backup.BackupScreen
import tr.com.gndg.self.ui.customers.CustomerFormDestination
import tr.com.gndg.self.ui.customers.CustomerFormScreen
import tr.com.gndg.self.ui.customers.CustomersDestination
import tr.com.gndg.self.ui.customers.CustomersScreen
import tr.com.gndg.self.ui.customers.presentation.CustomersViewModel
import tr.com.gndg.self.ui.home.HomeDestination
import tr.com.gndg.self.ui.home.HomeScreen
import tr.com.gndg.self.ui.inventory.InventoryDestination
import tr.com.gndg.self.ui.inventory.InventoryScreen
import tr.com.gndg.self.ui.inventory.InventoryTransactionDestination
import tr.com.gndg.self.ui.inventory.presentation.InventoryViewModel
import tr.com.gndg.self.ui.product.ProductDestination
import tr.com.gndg.self.ui.product.ProductScreen
import tr.com.gndg.self.ui.reports.ReportsDestination
import tr.com.gndg.self.ui.reports.ReportsScreen
import tr.com.gndg.self.ui.suppliers.SupplierFormDestination
import tr.com.gndg.self.ui.suppliers.SupplierFormScreen
import tr.com.gndg.self.ui.suppliers.SuppliersDestination
import tr.com.gndg.self.ui.suppliers.SuppliersScreen
import tr.com.gndg.self.ui.suppliers.presentation.SuppliersViewModel
import tr.com.gndg.self.ui.transactions.TransactionScreen
import tr.com.gndg.self.ui.transactions.TransactionsDestination
import tr.com.gndg.self.ui.transactions.newTransaction.NewTransactionScreen
import tr.com.gndg.self.ui.transactions.newTransaction.NewTransactionScreenDestination
import tr.com.gndg.self.ui.transactions.newTransaction.TransactionPortal
import tr.com.gndg.self.ui.transactions.newTransaction.TransactionPortalDestination
import tr.com.gndg.self.ui.transactions.newTransaction.TransactionWarehouseSelectScreen
import tr.com.gndg.self.ui.transactions.newTransaction.TransactionWarehouseSelectScreenDestination
import tr.com.gndg.self.ui.warehouses.WarehouseFormDestination
import tr.com.gndg.self.ui.warehouses.WarehouseFormScreen
import tr.com.gndg.self.ui.warehouses.WarehouseSelectDestination
import tr.com.gndg.self.ui.warehouses.WarehouseSelectScreen
import tr.com.gndg.self.ui.warehouses.WarehousesDestination
import tr.com.gndg.self.ui.warehouses.WarehousesScreen
import tr.com.gndg.self.ui.warehouses.presentation.WarehouseFormViewModel
import tr.com.gndg.self.ui.warehouses.presentation.WarehousesViewModel

@Composable
fun SelfNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route, modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToRoute = { navController.navigate(it) },
                warehouseSelectScreen = { navController.navigate(WarehouseSelectDestination.route) } )
        }

        composable(route = InventoryDestination.route) {

            val viewModel: InventoryViewModel = getViewModel()

            InventoryScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                toProductScreen =  { navController.navigate("${ProductDestination.route}/${it}") },
                warehouseSelectScreen = { navController.navigate(WarehouseSelectDestination.route) }
                )
        }

        composable(route = InventoryDestination.route) {

            val viewModel: InventoryViewModel = getViewModel()

            InventoryScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                toProductScreen =  { navController.navigate("${ProductDestination.route}/${it}") },
                warehouseSelectScreen = { navController.navigate(WarehouseSelectDestination.route) }
            )
        }

        composable(route = InventoryTransactionDestination.routeWithTransactionArgs ,
            arguments = listOf(navArgument(InventoryTransactionDestination.transactionIDArg) {
                defaultValue = 0L
                type = NavType.LongType } ),
        ) {

            val viewModel: InventoryViewModel = getViewModel()

            InventoryScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                toProductScreen =  { navController.navigate("${ProductDestination.route}/${it}") },
                warehouseSelectScreen = { navController.navigate(WarehouseSelectDestination.route) }
            )
        }

        composable(route = ProductDestination.routeWithUUIDArgs,
            arguments = listOf(navArgument(ProductDestination.uuidArg) {
                    nullable = true
                    type = NavType.StringType } ),
            )
        {
            ProductScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true,
                toSupplierScreen = { navController.navigate("${SuppliersDestination.route}/${it}") },
                toCameraScreen = { navController.navigate("${CameraDestination.route}/${it}") }
            )
        }

        composable(route = CameraDestination.routeWithUUIDArgs,
            arguments = listOf(navArgument(ProductDestination.uuidArg) {
                type = NavType.StringType } ),
        )
        {backStackEntry->

            val productUUID = backStackEntry.arguments?.getString(CameraDestination.uuidArg)

            CameraOpen(
                navigateBack = { navController.popBackStack() },
                productUUID = productUUID
            )
        }

        composable(route = WarehousesDestination.routeWithArgs ,
            arguments = listOf(navArgument(WarehousesDestination.stateOrListArg) {
                nullable = true
                type = NavType.StringType } ),) {

            val viewModel: WarehousesViewModel = getViewModel()

            WarehousesScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true,
                toWarehouseFormScreen = { navController.navigate("${WarehouseFormDestination.route}/${it}") },
                viewModel = viewModel
                )
        }
        composable(route = WarehouseFormDestination.routeWithArgs,
            arguments = listOf(navArgument(WarehouseFormDestination.warehouseUUIDArgs) {
                nullable = true
                type = NavType.StringType
            })
        ) {
            val viewModel: WarehouseFormViewModel = getViewModel()
            WarehouseFormScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp()  },
                viewModel = viewModel,
            )
        }

        composable(route = WarehouseSelectDestination.route) {
            WarehouseSelectScreen(
                navigateBack = { navController.popBackStack() },
                canNavigateBack = true,
            )
        }

        composable(route = TransactionsDestination.route) {
            TransactionScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { },
                warehouseSelectScreen = { navController.navigate(WarehouseSelectDestination.route) },
                selectTransaction = {
                    navController.navigate("${NewTransactionScreenDestination.route}/${it}")
                }
            )
        }

        composable(route = TransactionPortalDestination.routeWithArgs,
            arguments = listOf(navArgument(TransactionPortalDestination.transactionTypeArgs) {
                type = NavType.IntType
            })
        ) {
            TransactionPortal(
                navigateBack = { navController.navigate(HomeDestination.route) },
                toNewTransactionScreen = { navController.navigate(NewTransactionScreenDestination.route) {
                    popUpTo(HomeDestination.route) {
                        inclusive = false
                    }
                } },
                toTransactionWarehouseSelectScreen = {
                    navController.navigate("${TransactionWarehouseSelectScreenDestination.route}/${it}") {
                        popUpTo(HomeDestination.route) {
                            inclusive = false
                        }
                    }
                }

            )
        }

        composable(route = NewTransactionScreenDestination.route,

        ) {
            NewTransactionScreen(
                transactionID = null,
                navigateBack = { navController.navigate(HomeDestination.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                } },
                toInventoryScreen = { navController.navigate("${InventoryTransactionDestination.route}/${it}") },
                toSupplierScreen = { navController.navigate("${SuppliersDestination.route}/${it}") },
                toCustomerScreen = {navController.navigate("${CustomersDestination.route}/${it}") }
            )
        }

        composable(route = NewTransactionScreenDestination.routeWithTransactionIDArgs,
            arguments = listOf(navArgument(NewTransactionScreenDestination.transactionIDArg) {
                type = NavType.LongType
            })
            ) {backStackEntry->

            val transactionID = backStackEntry.arguments?.getLong(NewTransactionScreenDestination.transactionIDArg)

            NewTransactionScreen(
                transactionID = transactionID,
                navigateBack = { navController.popBackStack() },
                toInventoryScreen = { navController.navigate("${InventoryTransactionDestination.route}/${it}") },
                toSupplierScreen = { navController.navigate("${SuppliersDestination.route}/${it}") },
                toCustomerScreen = {navController.navigate("${CustomersDestination.route}/${it}") }
            )
        }

        composable(route = TransactionWarehouseSelectScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(TransactionWarehouseSelectScreenDestination.transactionTypeForWarehouseArgs) {
                type = NavType.IntType
            })
        ) {
            TransactionWarehouseSelectScreen(
                selectDoneNavigateBack = { navController.navigate("${TransactionPortalDestination.route}/${it}") }
            )
        }

        composable(route = SuppliersDestination.route

        ) {

            val transactionID : Long? = null

            SuppliersScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true,
                transactionID = transactionID,
                toSupplierFormScreen = { navController.navigate("${SupplierFormDestination.route}/${it}")}
            )
        }


        composable(route = SuppliersDestination.routeWithTransactionArgs,
            arguments = listOf(navArgument(SuppliersDestination.transactionIDArg) {
                type = NavType.LongType
            })
        ) {backStackEntry->

            val transactionID = backStackEntry.arguments?.getLong(SuppliersDestination.transactionIDArg)

            SuppliersScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true,
                transactionID = transactionID,
                toSupplierFormScreen = { navController.navigate("${SupplierFormDestination.route}/${it}")}
            )
        }

        composable(route = SupplierFormDestination.routeWithArgs,
            arguments = listOf(navArgument(SupplierFormDestination.supplierUUIDArgs) {
                nullable = true
                type = NavType.StringType
            })
        ) {backStackEntry->

            val supplierUUID = backStackEntry.arguments?.getString(SupplierFormDestination.supplierUUIDArgs)
            val supplierViewModel : SuppliersViewModel = getViewModel()

            SupplierFormScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp()  },
                canNavigateBack = true,
                supplierUUID = supplierUUID,
                viewModel = supplierViewModel
            )
        }

        composable(route = CustomersDestination.route) {

            val transactionID : Long? = null

            CustomersScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true,
                transactionID = transactionID,
                toCustomerFormScreen = {navController.navigate("${CustomerFormDestination.route}/${it}")}
            )
        }

        composable(route = CustomersDestination.routeWithTransactionArgs,
            arguments = listOf(navArgument(CustomersDestination.transactionIDArg) {
                type = NavType.LongType
            })
        ) {backStackEntry->

            val transactionID = backStackEntry.arguments?.getLong(CustomersDestination.transactionIDArg)

            CustomersScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true,
                transactionID = transactionID,
                toCustomerFormScreen = {navController.navigate("${CustomerFormDestination.route}/${it}")}
            )
        }

        composable(route = CustomerFormDestination.routeWithArgs,
            arguments = listOf(navArgument(CustomerFormDestination.customerUUIDArgs) {
                nullable = true
                type = NavType.StringType
            })
        ) {backStackEntry->

            val customerUUID = backStackEntry.arguments?.getString(CustomerFormDestination.customerUUIDArgs)
            val customerViewModel : CustomersViewModel = getViewModel()

            CustomerFormScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp()  },
                canNavigateBack = true,
                customerUUID = customerUUID,
                viewModel = customerViewModel
            )
        }

        composable(route = ReportsDestination.route) {

            ReportsScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true
            )
        }

        composable(route = BackupDestination.route) {

            BackupScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = true
            )
        }

    }
}