package tr.com.gndg.self

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import tr.com.gndg.self.di.LifecycleAwareTimer
import tr.com.gndg.self.di.TimerCallback
import tr.com.gndg.self.ui.theme.SelfTheme

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity(), TimerCallback {

    private var timer: LifecycleAwareTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val time = 3000

        startTimer(time.toLong())

        setContent {
            SelfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MySplashScreen()
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startTimer(duration: Long){
        timer?.discardTimer()
        timer = LifecycleAwareTimer(duration, 1000, this)
        timer?.startTimer()
    }


    override fun onTick(millisUntilFinished: Long) {
        println(millisUntilFinished)
    }

    override fun onTimeOut() {
        startMainActivity()
    }

}

@Composable
fun MySplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(model = R.mipmap.ic_launcher, contentDescription = "App Logo")
 }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    MySplashScreen()
}