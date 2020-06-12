package paralland.paralleluniverse.activities

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import paralland.paralleluniverse.BackgroundTask.JavaWrapper
import paralland.paralleluniverse.R
import paralland.paralleluniverse.coroutines.CoroutinesPi
import paralland.paralleluniverse.managers.CaculationManager

import paralland.paralleluniverse.managers.CalculationListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

 class MainActivity : AppCompatActivity(), CalculationListener {
    private var calcresult:Double =0.0
    private var executionTime:Long = 0
    private var numSteps:Long = 100000000
    private var numThreads:Long = 4
    private val NUMSTEPS = "Number of steps";
    private val NUMTHREADS = "Number of threads";
    private val PREF_FILE = "paralland_preferences";
    private val SERIAL = 1;
    private val JAVATHREADS = 2;
    private val COROUTINES = 3;
    private var  startJvThreads:Long =0
    private var  startSerial:Long =0
    private var  startCorout:Long =0
    private var cm : CaculationManager = CaculationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        numSteps = getSharedPreferences(PREF_FILE, 0).getLong(NUMSTEPS,100000000)
        numThreads = getSharedPreferences(PREF_FILE, 0).getLong(NUMTHREADS,4)

        runSerial.setOnClickListener { view ->

            if (startSerial.toInt() == 0) {
                Snackbar.make(view, R.string.calcSerial,
                    Snackbar.LENGTH_LONG).show()

                startSerial = System.currentTimeMillis()
                calculateSerial()
            }else{
                Snackbar.make(view, R.string.sorry, Snackbar.LENGTH_LONG)
            }

        }
        runJavath.setOnClickListener { view ->
            if (startJvThreads.toInt() == 0) {
                Snackbar.make(view, R.string.calcParallelThreads,
                    Snackbar.LENGTH_LONG)
                startJvThreads = System.currentTimeMillis()
                calculateJvThreads()
            }else{
                Snackbar.make(view, R.string.sorry, Snackbar.LENGTH_LONG)

            }
        }
        runCorout.setOnClickListener { view ->
            if (startCorout.toInt() == 0) {
                Snackbar.make(view, R.string.calcParallelCoroutines,
                    Snackbar.LENGTH_LONG)

                startCorout = System.currentTimeMillis()
                calculateCoroutines()
            }else{
                Snackbar.make(view, R.string.sorry, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
        clear.setOnClickListener { view ->
            showTextSerial.text =getString(R.string.result_Serial)
            showTextJavath.text =getString(R.string.result_Javath)
            showTextCorout.text =getString(R.string.result_Corout)
        }

        progressBarSerial.visibility = View.INVISIBLE
        progressBarJavaTh.visibility = View.INVISIBLE
        progressBarCorout.visibility = View.INVISIBLE
    }


     fun calculateSerial(){
         progressBarSerial.visibility = View.VISIBLE

         var calculation = Runnable { calcresult = cm.caculatePiD(numSteps)
         }
         doinBackground(calculation, SERIAL)

     }
     fun calculateJvThreads(){
         progressBarJavaTh.visibility = View.VISIBLE

         var javaWrap = JavaWrapper(this, numSteps, JAVATHREADS)
         javaWrap.doExcecute(numSteps)

     }
     fun calculateCoroutines(){
         progressBarCorout.visibility = View.VISIBLE

         var coroutinePi = CoroutinesPi()
         coroutinePi.runCoroutine(this,numSteps, COROUTINES)

     }



    private fun doinBackground(backgroundtask: Runnable, id:Int) {

        var hand = Handler()
        val tthread = Thread(Runnable {
             try {
                    hand.post(Runnable {

                    })
                   backgroundtask.run()
                    hand.post(Runnable {

                        onCalculationFinished(calcresult,id)
                    })
                } catch (e: Exception) { // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            })
        tthread.start()

    }
    override fun onCalculationFinished(result: Double, which:Int) {

        when (which) {
            SERIAL -> {
                executionTime = System.currentTimeMillis() - startSerial
                showTextSerial.text = (getString(R.string.done) + " " + getString(R.string.inSerial)
                        + " " + getString(R.string.result) + " " +
                        getString(R.string.time)).format(
                    numSteps,
                    cm.roundtotwelvedez(result), executionTime
                )

                progressBarSerial.visibility = View.INVISIBLE
                startSerial = 0;
            }
            JAVATHREADS -> {
                executionTime = System.currentTimeMillis() - startJvThreads

                showTextJavath.text =
                    (getString(R.string.done) + " " + getString(R.string.withJavaThreads)
                            + " " + getString(R.string.result) + " " +
                            getString(R.string.time)).format(
                        numSteps,
                        cm.roundtotwelvedez(result), executionTime
                    )

                progressBarJavaTh.visibility = View.INVISIBLE
                startJvThreads = 0;
            }
            COROUTINES -> {
                executionTime = System.currentTimeMillis() - startCorout

                showTextCorout.text =
                    (getString(R.string.done) + " " + getString(R.string.withCoroutines)
                            + " " + getString(R.string.result) + " " +
                            getString(R.string.time)).format(
                        numSteps,
                        cm.roundtotwelvedez(result), executionTime
                    )

                progressBarCorout.visibility = View.INVISIBLE
                startCorout = 0;
            }
        }
    }
 }
