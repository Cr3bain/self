package tr.com.gndg.self.core.dataSource.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.BrandDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.BrandDataDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.CategoryDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.CategoryDataDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.CompanyDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.CustomerDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.ExpenseDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.ImageUriDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.LabelDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.LabelDataDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.ProductDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.ProductPackDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.SizeDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.SizeDataDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.StockDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.SupplierDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.TaxDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.TransactionDao
import tr.com.gndg.self.core.dataSource.roomDatabase.dao.WarehouseDao
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.BrandEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CategoryEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CompanyEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.CustomerEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ExpenseEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ImageUriEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.LabelDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.LabelEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.ProductPackEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SizeDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SizeEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.StockEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.SupplierEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.TaxEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.WarehouseEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionDataEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.entity.transactions.TransactionEntity
import tr.com.gndg.self.core.dataSource.roomDatabase.util.LocalTypeConverter

@TypeConverters(LocalTypeConverter::class)
@Database(entities =
[ProductEntity::class,
    WarehouseEntity::class,
    BrandEntity::class,
    BrandDataEntity::class,
    CategoryEntity::class,
    CategoryDataEntity::class,
    CompanyEntity::class,
    CustomerEntity::class,
    ProductPackEntity::class,
    SupplierEntity::class,
    TaxEntity::class,
    StockEntity::class,
    ImageUriEntity::class,
    SizeEntity::class,
    SizeDataEntity::class,
    LabelEntity::class,
    LabelDataEntity::class,
    TransactionEntity::class,
    TransactionDataEntity::class,
    ExpenseEntity::class
                     ], version = 1, exportSchema = false)
abstract class SelfDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun warehouseDao() : WarehouseDao
    abstract fun brandDao() : BrandDao
    abstract fun brandDataDao() : BrandDataDao
    abstract fun categoryDao() : CategoryDao
    abstract fun categoryDataDao() : CategoryDataDao
    abstract fun companyDao() : CompanyDao
    abstract fun customerDao() : CustomerDao
    abstract fun productPackDao() : ProductPackDao
    abstract fun supplierDao(): SupplierDao
    abstract fun taxDao(): TaxDao
    abstract fun stockDao() : StockDao
    abstract fun imageUriDao() : ImageUriDao
    abstract fun sizeDao() : SizeDao
    abstract fun sizeDataDao() : SizeDataDao
    abstract fun labelDao() : LabelDao
    abstract fun labelDataDao() : LabelDataDao
    abstract fun transactionDao() : TransactionDao
    abstract fun expenseDao() :ExpenseDao

    companion object {

        @Volatile
        private var instance: SelfDatabase? = null
        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }

        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SelfDatabase::class.java,
            "selfDatabase")
            .allowMainThreadQueries().build()

    }

}