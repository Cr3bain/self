package tr.com.gndg.self.domain.repo

import kotlinx.coroutines.flow.Flow

interface BarcodeScannerRepository {

    fun startScanning(): Flow<String?>

}