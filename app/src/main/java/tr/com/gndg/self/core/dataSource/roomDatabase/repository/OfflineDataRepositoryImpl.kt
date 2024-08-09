package tr.com.gndg.self.core.dataSource.roomDatabase.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.core.dataSource.roomDatabase.SelfDatabase
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ImageUriEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.toCategoryDataJoin
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.toProductStocks
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.toProductTransaction
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.toProductsJoin
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.join.toTransactionJoinData
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toCategoryData
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toCustomer
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toProduct
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toStock
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toSupplier
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.toWarehouse
import tr.com.gndg.self.core.util.TransactionType
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
import tr.com.gndg.self.domain.model.toCategoryEntity
import tr.com.gndg.self.domain.model.toCustomerEntity
import tr.com.gndg.self.domain.model.toProductEntity
import tr.com.gndg.self.domain.model.toStockEntity
import tr.com.gndg.self.domain.model.toSupplierEntity
import tr.com.gndg.self.domain.model.toWarehouseEntity
import tr.com.gndg.self.domain.model.transactions.Transaction
import tr.com.gndg.self.domain.model.transactions.TransactionData
import tr.com.gndg.self.domain.model.transactions.TransactionDetail
import tr.com.gndg.self.domain.model.transactions.toTransactionDataEntity
import tr.com.gndg.self.domain.model.transactions.toTransactionEntity
import java.math.BigDecimal
import java.util.UUID

class OfflineDataRepositoryImpl(private val database: SelfDatabase) : DataSourceRepository {


    /**
     * data from  [ProductDao]
     */

    override fun getProduct(id: Long): Flow<Result<Product?>> {
        return flow {
            try {
                emit(Result.success( database.productDao().getProduct(id)?.toProduct()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    override fun getProduct(uuid: String): Product? {
        return database.productDao().getProductUUID(uuid)?.toProduct()
    }

    override fun getProducts(): Flow<Result<List<Product>>> {
        return flow {
            try {
                emit(Result.success( database.productDao().getProducts().map { it.toProduct() }))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    override fun setPrice(id: Long, newPrice: BigDecimal)  : Result<Boolean> {
       return try {
            database.productDao()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun updateProduct(product: Product) : Result<Boolean> {
        return try {
            database.productDao().update(
                productUUID = product.uuid,
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                stockCode= product.stockCode,
                minStock = product.minStock,
                avrPurchasePrice = product.avrPurchasePrice,
                purchasePrice = product.purchasePrice,
                sellPrice = product.sellPrice)
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }

    }

    override suspend fun insertProduct(product: Product) : Result<Boolean> {
        return try {
            database.productDao().insert(product.toProductEntity())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun getProductUUID(uuid: String): Flow<Result<Product?>> {
        return flow {
            try {
                emit(Result.success(database.productDao().getProductUUID(uuid)?.toProduct()))
            } catch (e: Exception){
                emit(Result.failure(e))
            }
        }
    }

    override fun getProductAndStocks(): List<ProductStocks> {
        return database.productDao().getProductAndStocks().map { it.toProductStocks() }
    }

    override fun getProductsJoin(): Flow<Result<List<ProductsJoin>>> {
        return flow {
            try {
                emit(Result.success(database.productDao().getProductsJoin().map { it.toProductsJoin() }))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    }

    override fun getProductJoin(productUUID: String): ProductsJoin? {
        return database.productDao().getProductJoin(productUUID)?.toProductsJoin()
    }

    override suspend fun updatePurchasePrice(productUUID: String, purchasePrice: BigDecimal) {
        return database.productDao().updatePurchasePrice(productUUID, purchasePrice)
    }

    override suspend fun getProductByBarcode(barcode: String): Product? {
        return database.productDao().getProductByBarcode(barcode)?.toProduct()
    }

    override suspend fun deleteProduct(product: Product): Result<Boolean> {
        return try {
            database.productDao().deleteProduct(product.uuid)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductsJoinList(): List<ProductsJoin> {
        return database.productDao().getProductsJoin().map { it.toProductsJoin() }
    }

    /**
     * data from  [WarehouseDao]
     */

    override fun getWarehouse(id: Long): Warehouse? {
        return database.warehouseDao().getWarehouse(id)?.toWarehouse()

    }

    override suspend fun getWarehouseUUID(uuid: String): Warehouse? {
        return database.warehouseDao().getWarehouseUUID(uuid)?.toWarehouse()
    }

    override fun getWarehouses(): List<Warehouse> {
        return database.warehouseDao().getWarehouses().map { it.toWarehouse() }
    }

    override suspend fun updateWarehouse(warehouse: Warehouse): Result<Boolean> {
        return try {
            database.warehouseDao().update(warehouse.name, warehouse.description, warehouse.uuid)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertWarehouse(warehouse: Warehouse): Result<Boolean> {
        return try {
            database.warehouseDao().insert(warehouse.toWarehouseEntity())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWarehouse(warehouseUUID: String) : Result<Boolean> {
        return try {
            database.warehouseDao().deleteWarehouse(warehouseUUID)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun warehouseIsEmpty(): Boolean {
        return database.warehouseDao().isEmpty()
    }

    override suspend fun deleteRequestWarehouse(warehouseUUID: String): Result<Boolean> {
        return try {
            Result.success(database.transactionDao().warehouseDeleteRequest(warehouseUUID))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * data from  [StockDao]
     */

    override suspend fun insertStock(stock: Stock) {
        database.stockDao().insertStock(stock.toStockEntity())
    }

    override suspend fun updateStock(newPiece: Float, uuid: String) {
        database.stockDao().updateStock(newPiece, uuid)
    }

    override suspend fun deleteStock(stock: Stock) {
        database.stockDao().deleteStock(stock.toStockEntity())
    }


    override fun getStocks(): List<Stock> {
        return database.stockDao().getStocks().map { it.toStock() }
    }

    override fun getWarehouseStocks(warehouseUUID: String): List<Stock> {
        return database.stockDao().getWarehouseStocks(warehouseUUID).map { it.toStock() }
    }

    override fun getProductStocks(productUUID: String): List<Stock> {
        return database.stockDao().getProductStocks(productUUID).map { it.toStock() }
    }

    override fun getWarehouseProductStocks(
        productUUID: String,
        warehouseUUID: String
    ): Stock? {
        return database.stockDao().getWarehouseProductStocks(productUUID, warehouseUUID)?.toStock()
    }

    /**
     * data from  [CategoryDataDao]
     */
    override suspend fun insertCategoryData(categoryData: CategoryData) {
        return database.categoryDataDao().insert(categoryData.toCategoryEntity())
    }

    override suspend fun updateCategoryData(categoryData: CategoryData) {
        return database.categoryDataDao().update(categoryData.toCategoryEntity())
    }

    override suspend fun deleteCategoryData(categoryData: CategoryData) {
        return database.categoryDataDao().delete(categoryData.toCategoryEntity())
    }

    override fun getProductCategoryData(productUUID: String): CategoryData? {
        return database.categoryDataDao().getProductCategoryData(productUUID)?.toCategoryData()
    }

    override fun deleteProductCategoryData(productUUID: String) {
        return database.categoryDataDao().deleteProductCategoryData(productUUID)
    }

    override fun getCategoryDataJoin(): CategoryDataJoin? {
        return database.categoryDataDao().getCategoryDataJoin()?.toCategoryDataJoin()
    }

    /**
     * data from  [TransactionDao]
     */
    override suspend fun insertTransaction(transaction: Transaction) {
        return database.transactionDao().insertTransaction(transaction.toTransactionEntity())
    }

    override suspend fun insertTransactionDataList(transactionData: TransactionData) {
        return database.transactionDao().insertTransactionData(transactionData.toTransactionDataEntity())
    }

    override suspend fun getProductTransactions(productUUID: String) : List<ProductTransaction> {
        return database.transactionDao().getProductTransactions(productUUID).map { it.toProductTransaction() }
    }

    override suspend fun getTransactionJoinDataList(): List<TransactionJoinData> {
        return database.transactionDao().getTransactionJoinDataList().map { it.toTransactionJoinData() }
    }

    override suspend fun getTransactions(): List<Transaction> {
        return database.transactionDao().getTransactions()
    }

    override suspend fun getTransactionByID(transactionID: Long): TransactionJoinData? {
        return database.transactionDao().getTransactionByID(transactionID)?.toTransactionJoinData()
    }

    override suspend fun getTransactionByUUID(transactionUUID: String): TransactionJoinData? {
        return database.transactionDao().getTransactionByUUID(transactionUUID)?.toTransactionJoinData()
    }

    override suspend fun deleteAllTransactionsDataByTransactionUUID(transactionUUID: String): Result<Boolean> {
        return  try {
            database.transactionDao().deleteAllTransactionsDataByTransactionUUID(transactionUUID)
            Result.success(true)
        } catch (e: Exception) {
            Log.e("transactionDao", e.message.toString())
            Result.success(false)
        }
    }

    override suspend fun deleteTransactionByUUID(uuid: String): Result<Boolean> {
        return  try {
            database.transactionDao().deleteTransactionByUUID(uuid)
            Result.success(true)
        } catch (e: Exception) {
            Log.e("transactionDao", e.message.toString())
            Result.success(false)
        }
    }

    override suspend fun deleteTransactionDataByUUID(transactionDataUUID: String) : Result<Boolean> {
        return  try {
            database.transactionDao().deleteTransactionDataByUUID(transactionDataUUID)
            Result.success(true)
        } catch (e: Exception) {
            Log.e("transactionDao", e.message.toString())
            Result.success(false)
        }
    }

    override suspend fun getLastTransactionID(): Long? {
        return database.transactionDao().getLastTransactionID()
    }

    override suspend fun updateTransactionDetail(transactionDetail: TransactionDetail): Result<Boolean> {
        return try {
            database.transactionDao().updateTransactionEntity(
                transactionID = transactionDetail.transactionID,
                sourceUUID = transactionDetail.sourceUUID,
                targetUUID = transactionDetail.targetUUID,
                sourceID = transactionDetail.sourceID,
                targetID = transactionDetail.targetID,
                description = transactionDetail.description,
                date = transactionDetail.date,
                discountPercent = transactionDetail.discountPercent,
                documentNumber = transactionDetail.documentNumber

            ).let {
                Result.success(true)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTransactionByType(transactionType: Int, id: Long?): List<TransactionJoinData> {
        return when {
            id != null && id != 0L -> {
                when (transactionType) {
                    TransactionType.Arrival -> {
                        //SourceID SATICI GETİRELECEK
                        database.transactionDao().getArrivalByTypeAndId(transactionType, id).map { it.toTransactionJoinData() }
                    }
                    TransactionType.Outgoing -> {
                        //targetID MÜŞTERİYE GÖRE ARAMA
                        database.transactionDao().getOutgoingByTypeAndId(transactionType, id).map { it.toTransactionJoinData() }
                    }
                    else -> {
                        database.transactionDao().getTransactionByType(transactionType).map { it.toTransactionJoinData() }
                    }
                }

            } else -> {
                database.transactionDao().getTransactionByType(transactionType).map { it.toTransactionJoinData() }
            }
        }

    }


    override suspend fun getSuppliers(): List<Supplier> {
        return database.supplierDao().getSuppliers().map { it.toSupplier() }
    }

    override suspend fun getSupplierUUID(supplierUUID: String): Supplier? {
        return database.supplierDao().getSupplierUUID(supplierUUID)?.toSupplier()
    }

    override suspend fun insertSupplier(supplier: Supplier): Result<Boolean> {
        return try {
            database.supplierDao().insertSupplier(supplier.toSupplierEntity())
            Result.success(true)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSupplier(supplier: Supplier): Result<Boolean> {
        return try {
            database.supplierDao().updateSupplier(
                supplierUUID = supplier.uuid,
                name = supplier.name,
                address = supplier.address,
                city = supplier.city,
                country = supplier.country,
                zipCode = supplier.zipCode,
                phoneNumber = supplier.phoneNumber,
                email = supplier.email,
                website = supplier.website,
                taxNumber = supplier.taxNumber,
                description = supplier.description
            )

            Result.success(true)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSupplier(supplier: Supplier): Result<Boolean> {
        return try {
            database.supplierDao().deleteSupplier(supplier.uuid)
            Result.success(true)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCustomers(): List<Customer> {
        return database.customerDao().getCustomers().map { it.toCustomer() }
    }

    override suspend fun getCustomerByUUID(customerUUID: String):Customer? {
        return database.customerDao().getCustomerByUUID(customerUUID)?.toCustomer()
    }

    override suspend fun insertCustomer(customer: Customer): Result<Boolean> {
        return try {
            database.customerDao().insertCustomer(customer.toCustomerEntity())
            Result.success(true)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCustomer(customer: Customer): Result<Boolean> {
        return try {
            database.customerDao().updateCustomer(
                customerUUID = customer.uuid,
                name = customer.name,
                address = customer.address,
                city = customer.city,
                country = customer.country,
                zipCode = customer.zipCode,
                phoneNumber = customer.phoneNumber,
                email = customer.email,
                website = customer.website,
                taxNumber = customer.taxNumber,
                description = customer.description
            )

            Result.success(true)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCustomer(customer: Customer): Result<Boolean> {
        return try {
            database.customerDao().deleteCustomer(customer.uuid)
            Result.success(true)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * data from  [ImageUriDao]
     */

    override suspend fun setProductImage(productUUID: String, imageUri: String?) {
        val imageUriEntity = database.imageUriDao().getProductImageUriUUID(productUUID)
        if (imageUriEntity == null) {
            database.imageUriDao().insertImageUri(
                ImageUriEntity(
                    uuid = UUID.randomUUID().toString(),
                    productUUID = productUUID,
                    imageUri = imageUri
                )
            )
        } else {
            database.imageUriDao().updateImageUri(productUUID, imageUri)
        }
    }

    override suspend fun getProductImage(productUUID: String): String? {
        return database.imageUriDao().getProductImageUriUUID(productUUID)?.imageUri
    }

    override suspend fun deleteImageUri(productUUID: String, imageUri: String?): Result<Boolean> {
        return try {
            database.imageUriDao().deleteImageUri(productUUID, imageUri)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}