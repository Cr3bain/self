package tr.com.gndg.self.data

import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import tr.com.gndg.self.domain.repo.BarcodeScannerRepository

class BarcodeScannerRepositoryImpl(private val scanner: GmsBarcodeScanner) : BarcodeScannerRepository {


    override fun startScanning(): Flow<String?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener {
                    launch {
                        send(getDetails(it))
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }
            awaitClose {  }
        }
    }

    private fun getDetails(barcode: Barcode): String {
        return when (barcode.valueType) {
            Barcode.TYPE_WIFI -> {
                val ssid = barcode.wifi!!.ssid
                val password = barcode.wifi!!.password
                val type = barcode.wifi!!.encryptionType
                "ssid : $ssid, password : $password, type : $type"
            }
            Barcode.TYPE_URL -> {
                "${barcode.url!!.url}"
            }
            Barcode.TYPE_PRODUCT -> {
                "${barcode.displayValue}"
            }
            Barcode.TYPE_EMAIL -> {
                "${barcode.email}"
            }
            Barcode.TYPE_CONTACT_INFO -> {
                "${barcode.contactInfo}"
            }
            Barcode.TYPE_PHONE -> {
                "${barcode.phone}"
            }
            Barcode.TYPE_CALENDAR_EVENT -> {
                "${barcode.calendarEvent}"
            }
            Barcode.TYPE_GEO -> {
                "${barcode.geoPoint}"
            }
            Barcode.TYPE_ISBN -> {
                "${barcode.displayValue}"
            }
            Barcode.TYPE_DRIVER_LICENSE -> {
                "${barcode.driverLicense}"
            }
            Barcode.TYPE_SMS -> {
                "${barcode.sms}"
            }
            Barcode.TYPE_TEXT -> {
                "${barcode.rawValue}"
            }
            Barcode.TYPE_UNKNOWN -> {
                "${barcode.rawValue}"
            }
            else -> {
                "0"
            }
        }

    }

}