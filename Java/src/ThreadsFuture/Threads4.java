package ThreadsFuture;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;

public class Threads4 implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        String name = Thread.currentThread().getName();
        return name;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        ArrayList<Future<String>> list = new ArrayList<Future<String>>();
        Callable<String> callable = new Threads4();
        for (int i = 0; i < 100; i++) {
            Future<String> future = executor.submit(callable);
            list.add(future);
        }

        for(Future<String> fut : list)
            try {
                System.out.println(new Date() + "::"+ fut.get());

            }catch (InterruptedException| ExecutionException e){
                e.printStackTrace();
            }
        executor.shutdown();
    }
}
