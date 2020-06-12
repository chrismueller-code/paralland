package paralland.paralleluniverse.BackgroundTask;

import android.app.Activity;
import android.os.AsyncTask;

public class JavaWrapper {
    private JavaPi javaPi;


    public JavaWrapper(Activity a, long numSteps, int id) {
        this.javaPi = new JavaPi(a,numSteps, id);

    }
    public void doExcecute(long numsteps){
        //we divide numSteps in numThreads parts we want to calculate in numThread threads
        long partSize = numsteps/4;
        long[] startend1 = new long[]{0,partSize};
        long[] startend2 = new long[]{partSize,(2*partSize)};
        long[] startend3 = new long[]{(2*partSize),(3*partSize)};
        long[] startend4 = new long[]{(3*partSize),(4*partSize)};

        javaPi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,startend1,startend2,startend3,startend4);
        //we use Thread_Pool_Excecuter which is supposed use parallel threads, but...

    }
}
