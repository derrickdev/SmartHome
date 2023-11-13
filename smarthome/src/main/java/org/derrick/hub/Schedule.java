package org.derrick.hub;

import org.derrick.devices.Conditioner;
import org.derrick.devices.Lamp;
import org.derrick.devices.Media;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Schedule {
    FileReader filereader;
    ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Lock lock = new ReentrantLock();


    public Schedule(FileReader filereader) {
        this.filereader = filereader;
    }
    public void shutdown() {
        System.out.println("Shut down SmartHome application");
        this.executor.shutdown();
    }
    public synchronized void run() {
        ConcurrentHashMap<Lamp, LocalTime[]> LampScheduling = filereader.getLampScheduling();
        ConcurrentHashMap<Media,LocalTime[]> MediaScheduling = filereader.getMediaScheduling();
        ConcurrentHashMap<Conditioner,LocalTime[]> ConditionerScheduling = filereader.getConditionerScheduling();
        List<String> conditionerState = filereader.getConditionerState();
        List<Integer> conditionerTemp = filereader.getConditionerTemp();

        for (ConcurrentHashMap.Entry<Media, LocalTime[]> entry : MediaScheduling.entrySet()) {
            Media media = entry.getKey();
            LocalTime[] schedule = entry.getValue();

            /*LocalTime begin = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")));*/
            LocalTime begin =LocalTime.now();
            LocalTime from = schedule[0];
            LocalTime to = schedule[1];
            int delay = (int) Duration.between(from, to).toMinutes();
            int start = (int) Duration.between(begin, from).toMinutes();
            int endTime = (int) Duration.between(begin,to).toMinutes();

            Runnable runnable = () -> {

                if((begin.equals(from) || begin.isAfter(from))  ) {
                    System.out.println(  media.getName() + " has already been  switch and will be over in " + endTime+ " minutes.");

                        media.setIsOn(true);
                        System.out.println(media.getName()  + " Statuts : " +  media.status());


                }
                else {
                    System.out.println("Request received successfully,  " + media.getName() + " will be switch in  " + start + " minutes.");
                    try {
                        Thread.sleep(start * 60000L);
                        System.out.println(media.getName()  + " Statuts : " +  media.status());
                        Thread.sleep(delay * 60000L);
                        media.setIsOn(!media.status());
                        System.out.println(media.getName() + " execution done , Statut : " + media.status());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }


            };

            executor.execute(runnable);


        }

        for (ConcurrentHashMap.Entry<Conditioner, LocalTime[]> entry : ConditionerScheduling.entrySet()) {
            Conditioner conditioner = entry.getKey();
            LocalTime[] schedule = entry.getValue();
            String conditionerCondition = conditionerState.get(0);

                Runnable runnable = () -> {
                    LocalTime begin =LocalTime.now();
                    LocalTime from = schedule[0];
                    LocalTime to = schedule[1];
                    int delay = (int) Duration.between(from, to).toMinutes();
                    int start = (int) Duration.between(begin, from).toMinutes();
                    int endTime = (int) Duration.between(begin,to).toMinutes();
                    System.out.println(conditionerCondition);

                    if(conditionerCondition.equalsIgnoreCase("ALWAYS")) {

                        System.out.println("Request received successfully,  " + conditioner.getName() + " will be switched always");

                        System.out.println(conditioner.getName()  + " statuts : " +  conditioner.status());
                        conditioner.setIsOn(conditioner.status());

                     }else  if((begin.equals(from) || begin.isAfter(from)) ) {


                        System.out.println(  conditioner.getName() + " has already been  switch and will be over in " + endTime+ " minutes.");

                        conditioner.setIsOn(true);
                        conditioner.setTemp(conditionerTemp.get(0)) ;
                        System.out.println(conditioner.getName()  + " Statuts : " +  conditioner.status());


                    }else{

                            System.out.println("Request received successfully,  " + conditioner.getName() + " will be switched in  " + start + " minutes.");
                            try {
                                Thread.sleep( start * 60000L);
                                System.out.println(conditioner.getName()  + " statuts : " +  conditioner.status());
                                Thread.sleep( delay * 60000L);
                                conditioner.setIsOn(!conditioner.status());
                                System.out.println(conditioner.getName() + " execution done , Statuts : " + conditioner.status());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                    }

            };

            executor.execute(runnable);




        }

        for (ConcurrentHashMap.Entry<Lamp, LocalTime[]> entry : LampScheduling.entrySet()) {
            Lamp lamp = entry.getKey();
            LocalTime[] schedule = entry.getValue();
            String conditionerCondition = conditionerState.get(0);
            LocalTime begin =LocalTime.now();
            LocalTime from = schedule[0];
            LocalTime to = schedule[1];

            int delay = (int) Duration.between(from, to).toMinutes();
            int start = (int) Duration.between(begin, from).toMinutes();
            int endTime = (int) Duration.between(begin,to).toMinutes();

            Runnable runnable = () -> {

                if(begin.equals(from) || begin.isAfter(from)) {
                    System.out.println(lamp.getName() + " has already been  switch and will be over in " + endTime + " minutes.");
                    try {
                        System.out.println(lamp.getName() + " Statuts : " + lamp.status());
                        Thread.sleep(delay * 60000L);
                        lamp.setIsOn(!lamp.status());
                        System.out.println(lamp.getName() + " execution done , Statut : " + lamp.status());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }else {

                        System.out.println("Request received successfully,  " + lamp.getName() + " will be switch in  " + start + " minutes.");
                        try {
                            Thread.sleep(start * 60000L);
                            System.out.println(lamp.getName()  + " Statuts : " +  lamp.status());
                            Thread.sleep(delay * 60000L);
                            lamp.setIsOn(!lamp.status());
                            System.out.println(lamp.getName() + " execution done , Statut : " + lamp.status());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }



            };
            executor.execute(runnable);



        }
    }
}
