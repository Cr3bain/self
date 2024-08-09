package tr.com.gndg.self.di

import android.os.CountDownTimer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Any lifecycle owner that need a timer should implement this.
 */
interface TimerCallback : LifecycleOwner {

    fun onTick(millisUntilFinished: Long)

    fun onTimeOut()
}

/**
 * Countdown timer that is aware of lifecycle of activity/fragment. It avoids need to implement
 * lifecycle methods on the caller end and enforce to implement `onTick` and `onTimeOut`. Also delivers
 * the timeout when activity/fragment resumed if the time is past.
 *
 * Caller should make sure to keep reference to the timer and start & discard to make sure duplicate
 * timers not running in parallel. Once the timer finish it will off-hook itself.
 *
 *
 * Usage:
 * ```kotlin
 * val timer: LifecycleAwareTimer? = null
 *
 * fun startMyTimer()
 * {
 *      val duration = 10*1000 // 10 sec timer
 *      timer?.discardTimer() // discard old timer to avoid
 *      timer = LifecycleAwareTimer(duration = duration, interval = 1000, this@SomeActivity/Fragment)
 *      timer?.startTimer()
 * }
 * ```
 */
class LifecycleAwareTimer(
    duration: Long,
    private val interval: Long,
    private val callback: TimerCallback
) : DefaultLifecycleObserver {

    private val stopAt: Long = System.currentTimeMillis() + duration
    private var timer: CountDownTimer? = null
    private val expired: Boolean
        get() = (stopAt - System.currentTimeMillis()) <= 0

    init {
        callback.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (expired) {
            callback.onTimeOut()
            discardTimer()
        } else {
            startTimer()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        timer?.cancel()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        discardTimer()
    }

    /**
     * Create and start a CountDownTimer if needed. Also discards the previous timer (since timer
     * cannot be resumed and always start at the initial eta).
     */
    fun startTimer() {
        timer?.cancel()
        timer = null

        val eta = stopAt - System.currentTimeMillis()
        timer = object : CountDownTimer(
            eta, interval) {
            override fun onTick(millisUntilFinished: Long) {
                callback.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                callback.onTimeOut()
                callback.lifecycle.removeObserver(this@LifecycleAwareTimer)
            }
        }
        timer?.start()
    }

    /**
     * Cancels the timer and off-hook from lifecycle callbacks
     */
    fun discardTimer() {
        timer?.cancel()
        callback.lifecycle.removeObserver(this)
    }

}