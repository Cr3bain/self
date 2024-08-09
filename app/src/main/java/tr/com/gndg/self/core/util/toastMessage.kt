package tr.com.gndg.self.core.util

import android.content.Context
import android.widget.Toast

fun toastMessage(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}