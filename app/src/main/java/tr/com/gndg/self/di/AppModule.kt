package tr.com.gndg.self.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tr.com.gndg.self.data.CustomerRepositoryImpl
import tr.com.gndg.self.data.ProductRepositoryImpl
import tr.com.gndg.self.data.StockRepositoryImpl
import tr.com.gndg.self.data.SupplierRepositoryImpl
import tr.com.gndg.self.data.TransactionRepositoryImpl
import tr.com.gndg.self.data.WarehouseRepositoryImpl
import tr.com.gndg.self.domain.repo.CustomerRepository
import tr.com.gndg.self.domain.repo.ProductRepository
import tr.com.gndg.self.domain.repo.StockRepository
import tr.com.gndg.self.domain.repo.SupplierRepository
import tr.com.gndg.self.domain.repo.TransactionRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository
import tr.com.gndg.self.ui.customers.presentation.CustomersViewModel
import tr.com.gndg.self.ui.home.data.HomeRepositoryImpl
import tr.com.gndg.self.ui.home.domain.HomeRepository
import tr.com.gndg.self.ui.home.presentation.HomeUIStateHolder
import tr.com.gndg.self.ui.home.presentation.HomeViewModel
import tr.com.gndg.self.ui.inventory.presentation.InventoryViewModel
import tr.com.gndg.self.ui.product.presentation.ProductViewModel
import tr.com.gndg.self.ui.reports.presentation.ReportsViewModel
import tr.com.gndg.self.ui.suppliers.presentation.SuppliersViewModel
import tr.com.gndg.self.ui.transactions.newTransaction.presentation.NewTransactionViewModel
import tr.com.gndg.self.ui.transactions.presentation.TransactionsViewModel
import tr.com.gndg.self.ui.warehouses.presentation.WarehouseFormViewModel
import tr.com.gndg.self.ui.warehouses.presentation.WarehousesViewModel

val appModule = module {

    //product
    singleOf(::ProductRepositoryImpl) {bind<ProductRepository>()}

    //warehouse
    singleOf(::WarehouseRepositoryImpl) { bind<WarehouseRepository>()}
    single<WarehouseRepository> { WarehouseRepositoryImpl(get()) }
    viewModelOf(::WarehousesViewModel)
    viewModelOf(::WarehouseFormViewModel)

    //HOME
    singleOf(::HomeRepositoryImpl) {bind<HomeRepository>()}
    viewModelOf(::HomeViewModel)
    factoryOf(::HomeUIStateHolder)


    //Inventory
    viewModelOf(::InventoryViewModel)

    //Product
    viewModelOf(::ProductViewModel)

    //Transaction
    singleOf(::TransactionRepositoryImpl) {bind<TransactionRepository>()}
    viewModelOf(::TransactionsViewModel)
    viewModelOf(::NewTransactionViewModel)

    //StockRepository
    singleOf(::StockRepositoryImpl) {bind<StockRepository>()}

    //supplier
    singleOf(::SupplierRepositoryImpl) {bind<SupplierRepository>()}
    viewModelOf(::SuppliersViewModel)

    //customer
    singleOf(::CustomerRepositoryImpl) { bind<CustomerRepository>()}
    viewModelOf(::CustomersViewModel)

    //reports
    viewModelOf(::ReportsViewModel)

}