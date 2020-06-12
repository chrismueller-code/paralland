package paralland.paralleluniverse.coroutines

import android.app.Activity
import paralland.paralleluniverse.managers.CalculationListener
import kotlinx.coroutines.*

class CoroutinesPi {

    lateinit var a:Activity
    lateinit var listener: CalculationListener

    var partSize:Long = 0
    var step: Double= 0.0
    lateinit var startend1:LongRange
    lateinit var startend2 :LongRange
    lateinit var startend3:LongRange
    lateinit var startend4 :LongRange
    var pi:Double = 0.0

     fun runCoroutine(activity: Activity, numberOfsteps:Long, id:Int){
        a= activity
        listener = activity as CalculationListener

         // We use GlobalScope.launch to start working in background
         GlobalScope.launch {
             calcPi(numberOfsteps)
             //this is on UiThread so withContext is needed
             withContext(Dispatchers.Main) {
                 listener.onCalculationFinished(pi, id)
             }
        }

    }

    suspend fun calcPi(numberOfsteps: Long):Double {
        partSize=numberOfsteps / 4
        step =1.0 / numberOfsteps;
        //we divide numSteps in four parts we want to calculate in four threads
        startend1 =0..partSize
        startend2 = (partSize + 1)until(2 * partSize)
        startend3 = ((2 * partSize) + 1)until(3 * partSize)
        startend4 = ((3 * partSize) + 1)until(4 * partSize-1)

        var partRes = calculateinParallel()

        for (partpi in partRes) {
            pi += partpi
        }
        return pi;
    }

    suspend fun calculateinParallel():List<Double> =
        coroutineScope {
            val deferreds = listOf(
                async { calculatePartly(startend1) },
                async { calculatePartly(startend2) },
                async { calculatePartly(startend3) },
                async { calculatePartly(startend4) }
            )
            //wait till all threads are finished
            deferreds.awaitAll()

        }

    fun calculatePartly(longRange: LongRange):Double{
        var tempsum = 0.0
            for (num in longRange) {
                val x: Double = (num + 0.5) * step
                tempsum += 4.0 / (1.0 + x * x)
            }
        return tempsum*step
    }

}