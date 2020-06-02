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
    var calcresult:Double =0.0
    var executionTime:Long = 0
    var numSteps:Long = 100000000
    var  start2:Long =0
    var  start1:Long =0
    var  start3:Long =0



    var cm : CaculationManager = CaculationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        run1.setOnClickListener { view ->

            if (start1.toInt() == 0) {
                Snackbar.make(view, R.string.calcSerial, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                start1 = System.currentTimeMillis()
                caclulate1()
            }else{
                Snackbar.make(view, R.string.sorry, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

        }
        run2.setOnClickListener { view ->
            if (start2.toInt() == 0) {
                Snackbar.make(view, R.string.calcParallelThreads, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                start2 = System.currentTimeMillis()
                caclulate2()
            }else{
                Snackbar.make(view, R.string.sorry, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
        run3.setOnClickListener { view ->
            if (start3.toInt() == 0) {
                Snackbar.make(view, R.string.calcParallelCoroutines, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                start3 = System.currentTimeMillis()
                caclulate3()
            }else{
                Snackbar.make(view, R.string.sorry, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
        clear.setOnClickListener { view ->
            showText1.text =getString(R.string.result_1)
            showText2.text =getString(R.string.result_2)
            showText3.text =getString(R.string.result_3)
        }

        progressBar1.visibility = View.INVISIBLE
        progressBar2.visibility = View.INVISIBLE
        progressBar3.visibility = View.INVISIBLE
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
     fun caclulate3(){
         progressBar3.visibility = View.VISIBLE
         //progressBar3.visibility = View.VISIBLE
         var coroutinePi: CoroutinesPi= CoroutinesPi()
         coroutinePi.runCoroutine(this,numSteps)

    }
    fun caclulate2(){

        progressBar2.visibility = View.VISIBLE

            var javaWrap: JavaWrapper = JavaWrapper(this, numSteps)
        javaWrap.doExcecute(numSteps)

    }

    fun caclulate1(){
        progressBar1.visibility = View.VISIBLE

            var calculation: Runnable = Runnable { calcresult = cm.caculatePiD(numSteps)
            var serialList = cm.serialDevided(numSteps)
            }
            doinBackground(calculation)


    }
    private fun doinBackground(backgroundtask: Runnable) {

            var hand: Handler  = Handler()
            val tthread = Thread(Runnable {
                try {
                    hand.post(Runnable {

                    })
                   backgroundtask.run()
                    hand.post(Runnable {

                        onCalculationFinished(calcresult,1)
                    })
                } catch (e: Exception) { // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            })
            tthread.start()

    }
    @VisibleForTesting
    override fun onCalculationFinished(result: Double, which:Int) {

        if (which == 1) {
            executionTime =System.currentTimeMillis() - start1
            showText1.text = (getString(R.string.done) + " "+getString(R.string.inSerial)
                    + " "+getString(R.string.result)+ " "+
                    getString(R.string.time)).format(numSteps,
                cm.roundtotwelvedez(result),executionTime)

            progressBar1.visibility = View.INVISIBLE
            start1 = 0;
        }else if (which == 2) {
            executionTime =System.currentTimeMillis() - start2

            showText2.text =(getString(R.string.done) + " "+getString(R.string.withJavaThreads)
                    + " "+getString(R.string.result)+ " "+
                    getString(R.string.time)).format(numSteps,
                cm.roundtotwelvedez(result),executionTime)


            progressBar2.visibility = View.INVISIBLE
            start2 = 0;
        }else{
            executionTime =System.currentTimeMillis() - start3

            showText3.text =(getString(R.string.done) + " "+getString(R.string.withCoroutines)
                    + " "+getString(R.string.result)+ " "+
                    getString(R.string.time)).format(numSteps,
                cm.roundtotwelvedez(result),executionTime)

            progressBar3.visibility = View.INVISIBLE
            start3 = 0;
        }
    }

}
