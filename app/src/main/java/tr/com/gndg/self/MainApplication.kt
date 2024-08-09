package tr.com.gndg.self

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.common.MlKit
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tr.com.gndg.self.core.dataSource.DataSourceRepository
import tr.com.gndg.self.core.dataSource.roomDatabase.SelfDatabase
import tr.com.gndg.self.core.dataSource.roomDatabase.repository.OfflineDataRepositoryImpl
import tr.com.gndg.self.core.preferences.sharedPreferencesBoolean
import tr.com.gndg.self.core.util.Constants
import tr.com.gndg.self.core.util.warehouseCheck
import tr.com.gndg.self.data.BarcodeScannerRepositoryImpl
import tr.com.gndg.self.di.appModule
import tr.com.gndg.self.domain.repo.BarcodeScannerRepository
import tr.com.gndg.self.domain.repo.WarehouseRepository

class MainApplication : Application() {

    private val database by lazy { SelfDatabase.invoke(this) }
    private val roomBackup by lazy { RoomBackup }
    private val warehouseRepository: WarehouseRepository by inject()

    override fun onCreate() {
        super.onCreate()

        var premiumUser by applicationContext.sharedPreferencesBoolean(Constants.PREMIUM)

        // ALL IS OPEN
        premiumUser = true

        try {
            MlKit.initialize(this)
        } catch (e: Exception) {
            Log.e("MlKit init", e.message.toString())
        }

        val options = GmsBarcodeScannerOptions.Builder()
            //.enableAutoZoom()
            .allowManualInput()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        val gmsBarcodeScanning = GmsBarcodeScanning.getClient(this, options)

        val moduleInstall = ModuleInstall.getClient(this)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(gmsBarcodeScanning)
            .build()
        moduleInstall
            .installModules(moduleInstallRequest)
            .addOnSuccessListener {
                if (it.areModulesAlreadyInstalled()) {
                    // Modules are already installed when the request is sent.
                    Log.v("GmsBarcodeScanning", "AlreadyInstalled")
                }
            }
            .addOnFailureListener {
                Log.e("MainApp", it.message.toString())
            }

        val roomModule = module {
            single {
                Room.databaseBuilder(
                    applicationContext,
                    SelfDatabase::class.java,
                    "selfDatabase"
                ).build()

            }
            single { database }

            single<DataSourceRepository> { OfflineDataRepositoryImpl(get()) }

            single { roomBackup }

        }

        val scannerModule = module {
            single { gmsBarcodeScanning }
            singleOf(::BarcodeScannerRepositoryImpl) { bind<BarcodeScannerRepository>()}
        }



        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(appModule, roomModule, scannerModule)

        }

        warehouseCheck(
            warehouseRepository = warehouseRepository,
            context = applicationContext
        )

    }

}