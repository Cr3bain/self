package tr.com.gndg.self.core.util

import android.Manifest
import android.content.Context
import android.os.Build
import tr.com.gndg.self.R

object Constants {
    const val IMAGE_FOLDER = "product_images"
    const val PDF_FOLDER = "pdf_folder"
    const val WAREHOUSE_LIMIT = 2
    const val BACKUP_FILE_NAME = "self_backup.sqlite3"
    const val PREMIUM = "PREMIUM"

    const val EMAIL_REGEX = "^[^\\s;]+@[^\\s;]+\\.[^\\s;]+(?:;[^\\s;]+@[^\\s;]+\\.[^\\s;]+)*\$"

    val BACKUP_PERMISSIONS: List<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            emptyList()
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
}

object TransactionType {
    const val Arrival : Int = 1
    const val Outgoing : Int = 2
    const val Transfer : Int = 3
    const val TransferTarget : Int = 4
    const val Return : Int = 5
}

fun returnTypeString(context: Context, transactionType: Int?) : String {
    return when(transactionType) {
        TransactionType.Arrival -> {
            context.getString(R.string.arrival)
        }
        TransactionType.Outgoing -> {
            context.getString(R.string.outgoings)
        }
        TransactionType.Transfer -> {
            context.getString(R.string.transfers)
        }
        TransactionType.Return -> {
            context.getString(R.string.returnTransaction)
        }

        else -> {
            "Closing...."
        }
    }
}

