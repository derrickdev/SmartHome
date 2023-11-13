package org.derrick.hub;

import org.derrick.devices.Conditioner;
import org.derrick.devices.Lamp;
import org.derrick.devices.Media;

public class deviceFactory <T>{
    public static <T> T create (String name , Devices type) {
        if(Devices.LAMP.equals(type)) {
            return (T) new Lamp(name);
        }else if(Devices.CONDITIONER.equals(type))  {
            return (T)  new Conditioner(name);
        }else if(Devices.MEDIA.equals(type)) {
            return (T) new Media(name);
        }
        return null;

    }
}
