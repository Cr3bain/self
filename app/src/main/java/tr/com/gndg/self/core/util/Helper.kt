package tr.com.gndg.self.core.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.regex.Pattern

fun numbersToCurrency(number: Int): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance("TRY")
    return numberFormat.format(number)
}

fun numbersToFormat(number: Int): String {
    val numberFormat = NumberFormat.getNumberInstance()
    return numberFormat.format(number)
}

fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Pattern.compile(Constants.EMAIL_REGEX).matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.isNotEmpty() && password.length >= 6
}

fun getCurrentDate(): Date = Calendar.getInstance().time

fun floatToString(float: Float?) : String {
    return try {
        return float?.toString() ?: "0.0"
    } catch (e: Exception) {
        "0.0"
    }
}

fun stringToFloat(string: String) : Float? {
    return string.toFloatOrNull()
}

fun stringToBigDecimal(string: String) : BigDecimal? {
    return try {
        string.toBigDecimal()
    } catch (e: Exception) {
        null
    }
}

fun bigDecimalToString(bigDecimal: BigDecimal?) : String {
    return try {
        bigDecimal?.toString() ?: ""
    } catch (e: Exception) {
        ""
    }
}

fun Context.findAndroidActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

fun dateTagForBackup() : String{
    return DateTime.now(DateTimeZone.forOffsetHours(3)).toString("dd_MM_yyyy_HH_mm_ss")
}