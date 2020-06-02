package paralland.paralleluniverse.BackgroundTask;

import android.app.Activity;
import android.os.AsyncTask;

import paralland.paralleluniverse.managers.CalculationListener;

public class JavaPi extends AsyncTask<long[], Integer, Double[]> {

    private Double[] result;
    private CalculationListener listener;
    private double step;



    public JavaPi(Activity callingActivity, long numSteps) {

        this.listener = (CalculationListener) callingActivity;
        this.result = new Double[]{0.0,0.0,0.0,0.0};
        this.step= 1.0 / numSteps;

    }


    @Override
    protected Double[] doInBackground(long[]... parts) {

        //four threads should be established for the four parts
        for (int i = 0;i<parts.length; i++) {
            double tempsum = 0.0;
            for (long num= parts[i][0];num<parts[i][1];num++) {
                double x = (num + 0.5) * step;
                tempsum += 4.0 / (1.0 + x * x);
            }
            result[i]= tempsum*step;
        }
        return result;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(Double[] result) {

   /* Now we have four results, we add up, which is critical, should be done here. Could also have used
        synchronized method in doinBackground */
        double pi = 0.0;

        for (double res: result) {
            pi += res;
        }

        listener.onCalculationFinished(pi, 2);


    }

}
