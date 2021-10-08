package jp.co.kiganix.android.example.adidtest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val targetSdk by lazy { findViewById<TextView>(R.id.targetSdk) }
    private val permissionDefined by lazy { findViewById<TextView>(R.id.permissionDefined) }
    private val button by lazy { findViewById<Button>(R.id.button) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        targetSdk.text = "targetSdk: ${BuildConfig.TARGET_SDK}"
        permissionDefined.text = "permissionDefined?: ${BuildConfig.PERMISSION_DEFINITION}"

        button.setOnClickListener {
            GlobalScope.launch {
                getInfo()
            }
        }
    }

    suspend fun getInfo() {
        val debugLogging = GlobalScope.async {
            return@async AdvertisingIdClient.getIsAdIdFakeForDebugLogging(this@MainActivity)
        }
        val info = GlobalScope.async {
            return@async AdvertisingIdClient.getAdvertisingIdInfo(this@MainActivity)
        }

        withContext(Dispatchers.Main) {
            debugLogging.await().let {
                if (it) {
                    Toast.makeText(this@MainActivity, "fakeForDebugLogging: true", Toast.LENGTH_SHORT).show()
                }
            }
            info.await().let {
                Toast.makeText(this@MainActivity, it.id ?: "null", Toast.LENGTH_LONG).show()
            }
        }
    }
}
