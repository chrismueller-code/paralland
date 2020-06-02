package paralland.paralleluniverse

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import paralland.paralleluniverse.BackgroundTask.JavaWrapper
import paralland.paralleluniverse.coroutines.CoroutinesPi
import paralland.paralleluniverse.managers.CaculationManager
import paralland.paralleluniverse.managers.CalculationListener
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class CalculationTests: TestApplicationListener  {


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("paralland.paralleluniverse", appContext.packageName)
    }
    @Test
    fun testCalculation(){
     val cm:CaculationManager= CaculationManager();
       assertEquals(3.1415926535, cm.caculatePiD(100000000),0.0000000001)

    }

    @get:Rule
    var testActivityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)
    var signal = CountDownLatch(1)
    @Test
    fun testAsyncCalculation(){

        val cm: CaculationManager = CaculationManager();
        val serialresult = cm.caculatePiD(100000000)
        val testActivity:TestActivity = testActivityRule.activity
        testActivity.setListener(this)

        val jwrap: JavaWrapper = JavaWrapper(testActivity, 100000000);
        var d: Double = 0.0

                jwrap.doExcecute(100000000)

        signal.await()
        //we have to wait until result is there
        d= testActivity.getResult()
        assertEquals(
            serialresult,d
            , 0.0000000001
        )

    }
    @Test
     fun testCourCalculation(){
        val cm: CaculationManager = CaculationManager();
        val cr: CoroutinesPi = CoroutinesPi();

        val serialresult = cm.caculatePiD(100000000)
        var d: Double = 0.0

        runBlocking {
        //we have to wait until result is there
                d = cr.calcPi(100000000)
        }
        assertEquals(
            serialresult,d
            , 0.0000000001
        )

    }


    class TestActivity : Activity(), CalculationListener {

        var pi: Double =-1.0
        lateinit var listener: TestApplicationListener

        fun setListener( test: CalculationTests){
            this.listener = test;
        }
        override fun onCalculationFinished(result: Double, which: Int) {
            pi = result;
            listener?.onTestResultAvailable()

        }

        fun getResult(): Double {
            return this.pi

        }
    }

    override fun onTestResultAvailable() {
        signal.countDown()
    }

}







