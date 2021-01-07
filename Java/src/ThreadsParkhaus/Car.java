package ThreadsParkhaus;

public class Car extends Thread{
    String name;
    ParkingGarage garage;

    public Car(String autoname, ParkingGarage garage) {
        this.name = autoname;
        this.garage = garage;
    }

    @Override
    public void run() {
        while (true){
            try {
                int timeR = (int) (Math.random()*10000);
                Thread.sleep(timeR);
                System.out.print(name+": ");
                garage.enter();

                timeR = (int) (Math.random()*10000);
                Thread.sleep(timeR);
                System.out.print(name+": ");
                garage.leave();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }
}
