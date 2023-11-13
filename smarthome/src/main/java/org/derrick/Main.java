package org.derrick;


import org.derrick.hub.FileReader;
import org.derrick.hub.Schedule;

public class Main {
    public static void main(String[] args) {
      FileReader fileReader = new FileReader("/home/back-end/Téléchargements/smarthome/src/input.txt");
      fileReader.reader();
      Schedule schedule = new Schedule(fileReader);
      schedule.run();
      fileReader.getInformation();
    }
}