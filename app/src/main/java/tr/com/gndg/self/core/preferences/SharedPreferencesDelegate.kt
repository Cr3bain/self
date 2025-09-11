package tr.com.gndg.self.core.preferences

import android.content.Context
import androidx.activity.ComponentActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import androidx.core.content.edit

class SharedPreferencesStringDelegate(
    context: Context,
    private val name :String,
    private val defaultValue : String = ""
) : ReadWriteProperty<Any?, String> {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("self_prefs", Context.MODE_PRIVATE)
    }
    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return sharedPreferences.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        return sharedPreferences.edit { putString(name, value) }
    }

}

class SharedPreferencesLongDelegate(
    context: Context,
    private val name: String,
    private val defaultValue : Long = -1L
) : ReadWriteProperty<Any?, Long> {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("self_prefs", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        return sharedPreferences.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
         return sharedPreferences.edit { putLong(name, value) }
    }


}

class SharedPreferencesBooleanDelegate(
    context: Context,
    private val name: String,
    private val defaultValue : Boolean = false
) : ReadWriteProperty<Any?, Boolean> {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("self_prefs", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return sharedPreferences.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        return sharedPreferences.edit { putBoolean(name, value) }
    }


}


fun Context.sharedPreferencesString(name: String) = SharedPreferencesStringDelegate(this, name)

fun Context.sharedPreferencesLong(name: String) = SharedPreferencesLongDelegate(this, name)

fun Context.sharedPreferencesBoolean(name: String) = SharedPreferencesBooleanDelegate(this, name)