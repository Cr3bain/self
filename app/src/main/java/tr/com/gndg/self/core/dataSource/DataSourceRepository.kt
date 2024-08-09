package tr.com.gndg.self.core.dataSource

import kotlinx.coroutines.flow.Flow
import tr.com.gndg.self.domain.join.CategoryDataJoin
import tr.com.gndg.self.domain.join.ProductStocks
import tr.com.gndg.self.domain.join.ProductTransaction
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.domain.join.TransactionJoinData
import tr.com.gndg.self.domain.model.CategoryData
import tr.com.gndg.self.domain.model.Customer
import tr.com.gndg.self.domain.model.Product
import tr.com.gndg.self.domain.model.Stock
import tr.com.gndg.self.domain.model.Supplier
import tr.com.gndg.self.domain.model.Warehouse
import tr.com.gndg.self.domain.model.transactions.Transaction
import tr.com.gndg.self.domain.model.transactions.TransactionData
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import java.math.BigDecimal

interface DataSourceRepository {

    /***
     * data from  [ProductDao]
     */
    fun getProduct(id: Long) : Flow<Result<Product?>>
    fun getProducts(): Flow<Result<List<Product>>>
    fun setPrice(id: Long, newPrice: BigDecimal) : Result<Boolean>
    suspend fun updateProduct(product: Product) : Result<Boolean>
    suspend fun insertProduct(product: Product) : Result<Boolean>
    fun getProductUUID(uuid: String) : Flow<Result<Product?>>
    fun getProduct(uuid: String) : Product?
    fun getProductAndStocks(): List<ProductStocks>
    fun getProductsJoin(): Flow<Result<List<ProductsJoin>>>
    fun getProductJoin(productUUID: String) : ProductsJoin?
    suspend fun updatePurchasePrice(productUUID: String, purchasePrice: BigDecimal)
    suspend fun getProductByBarcode(barcode: String) : Product?
    suspend fun deleteProduct(product: Product) : Result<Boolean>
    suspend fun getProductsJoinList() : List<ProductsJoin>


    /**
    * data from  [WarehouseDao]
    */
    fun getWarehouse(id: Long) : Warehouse?
    suspend fun getWarehouseUUID(uuid: String) : Warehouse?
    fun getWarehouses() : List<Warehouse>
    suspend fun updateWarehouse(warehouse: Warehouse) : Result<Boolean>
    suspend fun insertWarehouse(warehouse: Warehouse) : Result<Boolean>
    suspend fun deleteWarehouse(warehouseUUID: String) : Result<Boolean>
    fun warehouseIsEmpty() : Boolean
    suspend fun deleteRequestWarehouse(warehouseUUID: String) : Result<Boolean>

    /**
     * data from  [StockDao]
     */

    suspend fun insertStock(stock: Stock)
    suspend fun updateStock(newPiece: Float, uuid: String)
    suspend fun deleteStock(stock: Stock)
    fun getStocks(): List<Stock>
    fun getWarehouseStocks(warehouseUUID: String): List<Stock>
    fun getProductStocks(productUUID: String): List<Stock>
    fun getWarehouseProductStocks(productUUID: String, warehouseUUID: String): Stock?

    /**
     * data from  [CategoryDataDao]
     */
    suspend fun insertCategoryData(categoryData: CategoryData)
    suspend fun updateCategoryData(categoryData: CategoryData)
    suspend fun deleteCategoryData(categoryData: CategoryData)
    fun getProductCategoryData(productUUID: String): CategoryData?
    fun deleteProductCategoryData(productUUID: String)
    fun getCategoryDataJoin(): CategoryDataJoin?

    /**
     * data from  [TransactionDao]
     */
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun insertTransactionDataList(transactionData: TransactionData)
    suspend fun getProductTransactions(productUUID: String) : List<ProductTransaction>
    suspend fun getTransactionJoinDataList(): List<TransactionJoinData>
    suspend fun getTransactions(): List<Transaction>
    suspend fun getTransactionByID(transactionID: Long) : TransactionJoinData?
    suspend fun getTransactionByUUID(transactionUUID: String) : TransactionJoinData?
    suspend fun deleteAllTransactionsDataByTransactionUUID(transactionUUID : String) : Result<Boolean>
    suspend fun deleteTransactionByUUID(uuid : String): Result<Boolean>
    suspend fun deleteTransactionDataByUUID(transactionDataUUID : String) : Result<Boolean>
    suspend fun getLastTransactionID() : Long?
    suspend fun updateTransactionDetail(transactionDetail: TransactionDetail) : Result<Boolean>
    suspend fun getTransactionByType(transactionType: Int, id: Long?) : List<TransactionJoinData>
    /**
     * data from  [SupplierDao]
     */

    suspend fun getSuppliers() : List<Supplier>
    suspend fun getSupplierUUID(supplierUUID: String) : Supplier?
    suspend fun insertSupplier(supplier: Supplier) : Result<Boolean>
    suspend fun updateSupplier(supplier: Supplier) : Result<Boolean>
    suspend fun deleteSupplier(supplier: Supplier) : Result<Boolean>

    /**
     * data from  [CustomerDao]
     */

    suspend fun getCustomers() : List<Customer>
    suspend fun getCustomerByUUID(customerUUID: String) : Customer?
    suspend fun insertCustomer(customer: Customer) : Result<Boolean>
    suspend fun updateCustomer(customer: Customer) : Result<Boolean>
    suspend fun deleteCustomer(customer: Customer) : Result<Boolean>

    /**
     * data from  [ImageUriDao]
     */

    suspend fun setProductImage(productUUID: String, imageUri: String?)
    suspend fun getProductImage(productUUID: String) : String?
    suspend fun deleteImageUri(productUUID: String, imageUri: String?) : Result<Boolean>
}