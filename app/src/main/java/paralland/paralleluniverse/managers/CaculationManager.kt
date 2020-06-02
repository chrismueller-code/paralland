package paralland.paralleluniverse.managers

class CaculationManager{



    fun caculatePi(numSteps:Long):String{

        return (""+caculatePiD(numSteps));
    }

    fun caculatePiD(numSteps:Long):Double{

        var step =1.0 / numSteps;
        var x = 0.0;
        var pi = 0.0;
        var sum = 0.0;
              for (num in 0..numSteps-1) {
                x = (num + 0.5) * step;
                sum += 4.0 / (1.0 + x * x);

            }
            pi = sum*step

       return pi;
    }
    fun serialDevided(numSteps:Long):Double{
        var partSize=numSteps / 4
        var step =1.0 / numSteps;
        var x = 0.0;
        var pi = 0.0;
        var sum = 0.0;
        var part1:Double
        var part2:Double
        var part3:Double
        var part4:Double
        var temps1:Double=0.0
        var temps2:Double=0.0
        var temps3:Double=0.0
        var temps4:Double=0.0
        for (num in 0..partSize) {
            x = (num + 0.5) * step;
            temps1 += 4.0 / (1.0 + x * x);

        }
        part1 = temps1*step
        for (num in partSize+1..2*partSize) {
            x = (num + 0.5) * step;
            temps2 += 4.0 / (1.0 + x * x);

        }

        part2 = temps2*step

        for (num in 2*partSize+1..3*partSize) {
            x = (num + 0.5) * step;
            temps3 += 4.0 / (1.0 + x * x);

        }

        part3 = temps3*step

        for (num in 3*partSize+1..4*partSize-1) {
            x = (num + 0.5) * step;
            temps4 += 4.0 / (1.0 + x * x);

        }
        part4 = temps4*step

        return (part1+part2+part3+part4);
    }

    fun roundtotwelvedez(d: Double): Double {
        val dmal100 = d * 1000000000000
        val int100: Long = Math.round(dmal100)
        return (int100.toDouble() / 1000000000000)
    }

}
