package org.derrick.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.derrick.devices.Conditioner;
import org.derrick.devices.Lamp;
import org.derrick.devices.Media;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
@ToString

public class FileReader {
    private final Path file;
    private final List<Lamp> lampList;
    private final List<Media> mediaList;
    private final List<Conditioner> conditionerList;
    private final ConcurrentHashMap<Lamp,LocalTime[]> LampScheduling;
    private final ConcurrentHashMap<Media,LocalTime[]> MediaScheduling;



    private final ConcurrentHashMap<Conditioner,LocalTime[]> ConditionerScheduling;
    private final List<String> conditionerState ;
    private final List<Integer> conditionerTemp;

    public final ArrayList stateInfo;

    public FileReader(String filePath) {
        this.file = Paths.get(filePath);
        this.lampList = Collections.synchronizedList(new ArrayList<>() ) ;
        this.mediaList = Collections.synchronizedList(new ArrayList<>() ) ;
        this.conditionerList = Collections.synchronizedList(new ArrayList<>() ) ;
        this.LampScheduling = new ConcurrentHashMap<>();
        this.MediaScheduling = new ConcurrentHashMap<>();
        this.ConditionerScheduling = new ConcurrentHashMap<>();
        this.conditionerState = new ArrayList<>();
        this.conditionerTemp = new ArrayList<>();
        this.stateInfo = new ArrayList<>();
        
    }

    public void reader( ) {

        try {
           BufferedReader fileReader = Files.newBufferedReader(file);
           while (fileReader.ready()) {

               String s  = fileReader.readLine();
               String [] LineBreak = s.split(" ");
               ArrayList<String> finalvalue = new ArrayList<>() ;

                for (String value : LineBreak ) {
                    finalvalue.add(value);
                }
               if(finalvalue.get(0).equalsIgnoreCase(Devices.LAMP.toString()))    {
                   Lamp lampSample = deviceFactory.create(finalvalue.get(1),Devices.LAMP);
                 //  lampSample.setIsOn(finalvalue.get(2).equalsIgnoreCase("on"));
                   if (finalvalue.get(3).equalsIgnoreCase("from")) {
                       LocalTime from = LocalTime.parse(finalvalue.get(4));
                       LocalTime to = LocalTime.parse(finalvalue.get(6));
                       LampScheduling.put(lampSample, new LocalTime[]{from,to});
                   }
                   lampList.add(lampSample);
                   
               }else if (finalvalue.get(0).equalsIgnoreCase(Devices.MEDIA.toString())) {
                   Media mediaSample = deviceFactory.create(finalvalue.get(1),Devices.MEDIA);
                  // mediaSample.setIsOn(finalvalue.get(2).equalsIgnoreCase("on"));
                   if (finalvalue.get(3).equalsIgnoreCase("from")) {
                       LocalTime from = LocalTime.parse(finalvalue.get(4));
                       LocalTime to = LocalTime.parse(finalvalue.get(6));
                       MediaScheduling.put(mediaSample, new LocalTime[]{from,to});
                   }
                   mediaList.add(mediaSample);
               }else if (finalvalue.get(0).equalsIgnoreCase(Devices.CONDITIONER.toString())) {
                   Conditioner conditionerSample = deviceFactory.create(finalvalue.get(1),Devices.CONDITIONER);
                   conditionerSample.setTemp(Integer.parseInt(finalvalue.get(2)));
                   if (finalvalue.get(3).equalsIgnoreCase("from")) {
                       LocalTime from = LocalTime.parse(finalvalue.get(4));
                       LocalTime to = LocalTime.parse(finalvalue.get(6));
                       ConditionerScheduling.put(conditionerSample, new LocalTime[]{from,to});
                   } else if (finalvalue.get(3).equalsIgnoreCase("ALWAYS")) {
                       conditionerState.add(finalvalue.get(3));
                   }else  {
                        conditionerTemp.add(Integer.parseInt(finalvalue.get(3))) ;
                   }
                   conditionerList.add(conditionerSample);
               } else {
                   throw new Exception("Invalid input , please check if your file match with all the requirements");
               }


           }
           fileReader.close();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }

    }
    public synchronized void getInformation () {

            for (Lamp lamp : lampList) {
                System.out.println(lamp);
                stateInfo.add(lamp);
                LocalTime[] schedule = LampScheduling.get(lamp);
                if (schedule != null) {
                    System.out.println("Planning : " + Arrays.toString(schedule));


                }
            }

            for (Media media : mediaList) {
                System.out.println(media);
                stateInfo.add(media);
                LocalTime[] schedule = MediaScheduling.get(media);
                if (schedule != null) {
                    System.out.println("Planning : " + Arrays.toString(schedule));


                }
            }
        for (Conditioner conditioner : conditionerList) {
            System.out.println(conditioner);
            stateInfo.add(conditioner);
            LocalTime[] schedule = ConditionerScheduling.get(conditioner);
            if (schedule != null) {
                System.out.println("Planning : " + Arrays.toString(schedule));


            }
        }


    }

}
