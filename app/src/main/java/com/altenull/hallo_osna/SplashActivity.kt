package com.altenull.hallo_osna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.altenull.hallo_osna.api.StudentRetrofit
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.databinding.ActivitySplashBinding
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSplashActivity()
    }

    /**
     * Wait until followings are finished and then start `MainActivity`
     *  - Splash animation
     *  - Request `getStudents` API
     */
    private fun initSplashActivity() {
        CoroutineScope(Dispatchers.Default).launch {
            val deferredIsSplashAnimFinished: Deferred<Boolean> =
                async(Dispatchers.Main) { startSplashAnim() }
            val deferredStudents: Deferred<List<Student>> = async(Dispatchers.IO) { getStudents() }

            deferredIsSplashAnimFinished.await() // Just wait until animation is finished.
            val students: List<Student> = deferredStudents.await()

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putParcelableArrayListExtra(Constants.IntentKeys.Students, ArrayList(students))
            startActivity(intent)
            finish() // Finish `SplashActivity` permanently
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private suspend fun startSplashAnim(): Boolean {
        val animDuration = 1000L
        val animDelay = 1000L

        YoYo.with(Techniques.RubberBand)
            .duration(animDuration)
            .delay(animDelay)
            .playOn(binding.splashSymbol)

        delay(animDuration + animDelay)

        return true
    }

    private suspend fun getStudents(): List<Student> {
        return withContext(Dispatchers.IO) {
            val response = StudentRetrofit.studentService.getStudents()
            var students: List<Student> = emptyList()

            if (response.isSuccessful) {
                students = response.body() ?: emptyList()
            } else {
                Log.e(TAG, "response code: ${response.code()}")
                Log.e(TAG, "response message: ${response.message()}")
            }

            return@withContext students
        }
    }

    companion object {
        const val TAG = "SplashActivity"
    }
}