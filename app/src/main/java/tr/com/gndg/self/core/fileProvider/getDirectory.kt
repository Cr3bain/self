package tr.com.gndg.self.core.fileProvider

import android.content.Context
import android.util.Log
import tr.com.gndg.self.core.util.Constants
import java.io.File

fun getImageDirectory(context: Context): File? {
    try {
        val appDir = context.filesDir
        val imagesDirectory = File(appDir, Constants.IMAGE_FOLDER)
        val mediaDir = imagesDirectory.mkdirs()
        return if (imagesDirectory.exists()) {
            imagesDirectory
        } else {
            if (mediaDir) {
                imagesDirectory
            } else {
                null
            }
        }
    } catch (e: Exception) {
        Log.e("getDirectory", e.message.toString())
        return null
    }
}

fun getPDFDirectory(context: Context): File? {
    try {
        val appDir = context.cacheDir
        val pdfDirectory = File(appDir, Constants.PDF_FOLDER)
        val mediaDir = pdfDirectory.mkdirs()
        return if (pdfDirectory.exists()) {
            pdfDirectory
        } else {
            if (mediaDir) {
                pdfDirectory
            } else {
                null
            }
        }
    } catch (e: Exception) {
        Log.e("getPDFDirectory", e.message.toString())
        return null
    }
}