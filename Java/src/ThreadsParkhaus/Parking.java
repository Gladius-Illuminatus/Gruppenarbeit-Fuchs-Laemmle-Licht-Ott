package ThreadsParkhaus;

public class Parking {
    public static void main(String[] args) {
    ParkingGarage garage = new ParkingGarage(10);
    Car [] carNr = new Car[10];
        for (int i = 0; i < carNr.length; i++) {
           carNr[i] = new Car("Lord_Carl"+i,garage);
           carNr[i].start();
        }

    }
}
