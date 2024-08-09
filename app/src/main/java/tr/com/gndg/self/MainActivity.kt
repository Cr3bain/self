package tr.com.gndg.self

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import org.koin.compose.KoinContext
import tr.com.gndg.self.ui.theme.SelfTheme

class MainActivity : ComponentActivity() {

    lateinit var roomBackup: RoomBackup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomBackup = RoomBackup(this)
        content()
    }

    private fun content() {

        setContent {
            SelfTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    KoinContext {
                        SelfApp()
                    }

                }

            }
        }
    }

}
