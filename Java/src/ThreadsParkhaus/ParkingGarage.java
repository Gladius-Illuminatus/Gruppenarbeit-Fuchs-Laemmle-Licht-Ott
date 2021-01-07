package ThreadsParkhaus;

import java.sql.SQLOutput;

public class ParkingGarage {
    int space;
    int taken;

    public ParkingGarage(int space) {
        this.space = space;
        this.taken = 0;
    }

    public void enter(){
        if(taken < space) {
            taken++;
            System.out.println("Forsooth! Telleth it all and telleth it far! For I have arrived in this resting place and" +
                    " left " + (space-taken) + " spaces without a lord!");
        }else{
            System.out.println("A most notable coward, an infinite and endless liar, an hourly promise breaker, the owner of no one good quality! Mine Space hath been taken!");
        }

    }
    public void leave(){
        taken--;
        System.out.println("Alas this place too must be left. And "+(space-taken)+" spaces go without a lord!");
    }

}
