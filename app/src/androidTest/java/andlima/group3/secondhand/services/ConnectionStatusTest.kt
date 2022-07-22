package andlima.group3.secondhand.services

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

@DelicateCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ConnectionStatusTest: TestCase() {

    private lateinit var connection : ConnectionStatus

    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()

        connection = ConnectionStatus(context)

    }

    public override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun connectionTest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        connection = ConnectionStatus(context)

        var result = false

        GlobalScope.launch {
            connection.observeForever {
                result = it
            }
        }

        val getResult = result
    }
}