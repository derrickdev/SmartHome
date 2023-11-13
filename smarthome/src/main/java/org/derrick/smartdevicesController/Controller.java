package org.derrick.smartdevicesController;

import io.javalin.http.Context;
import org.derrick.devices.Conditioner;
import org.derrick.devices.Lamp;
import org.derrick.devices.Media;
import org.derrick.hub.FileReader;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller{
    static FileReader fileReader =  new FileReader("/home/back-end/Téléchargements/smarthome/src/input.txt");
    static ArrayList<String> stateInfo = fileReader.stateInfo;




    public static void getFileReaderInfo(Context context) {
        ArrayList<Object> values = new ArrayList<>();
        for (Object value: stateInfo) {
            values.add(value);
        }

        fileReader.getInformation();
        context.json(values);


    }
}
