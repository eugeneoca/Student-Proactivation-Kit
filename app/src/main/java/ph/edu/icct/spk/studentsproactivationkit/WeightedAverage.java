package ph.edu.icct.spk.studentsproactivationkit;

/**
 * Created by Alfred on 10/25/2016.
 */
public class WeightedAverage {
    double average = 0;
    public WeightedAverage(double grade) {
        if(grade>=97){
            this.average = 1.00;
        }else if(grade>=94){
            this.average = 1.25;
        }else if(grade>=91){
            this.average = 1.50;
        }else if(grade>=88){
            this.average = 1.75;
        }else if(grade>=85){
            this.average = 2.00;
        }else if(grade>=82){
            this.average = 2.25;
        }else if(grade>=79){
            this.average = 2.50;
        }else if(grade>=76){
            this.average = 2.75;
        }else if(grade>=75){
            this.average = 3.00;
        }else{
            this.average = 5.00;
        }
    }

    public double getAverage(){
        return this.average;
    }
}
