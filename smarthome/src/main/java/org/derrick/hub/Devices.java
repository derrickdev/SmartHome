package org.derrick.hub;

public enum Devices {
    LAMP,
    MEDIA,
    CONDITIONER;
    public static String getDeviceType(String type) {
        String DeviceType = "";
        for (Devices device: Devices.values()) {
            if(device.toString().equalsIgnoreCase(type)) {
                DeviceType= DeviceType + device.toString();
            } else {
                System.out.println("Wrong  device type ");
                break;
            }
        }
        return DeviceType;
    }
/*public  static  void  list() {
    for (Devices value: Devices.values()
         ) {
        System.out.println(" " + value);
    }
    }*/
}
