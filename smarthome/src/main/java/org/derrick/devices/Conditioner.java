package org.derrick.devices;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter

public class Conditioner implements deviceController {
    private String name;
    private Boolean isOn;
    private int temp;

    public Conditioner (String name) {
        this.name=name;
        this.isOn=false;
    }

    @Override
    public void changeState(Boolean state) {
            this.isOn=state;
    }

    @Override
    public Boolean status() {
       /* if(isOn) {
            System.out.println("Is On");

        }else{
            System.out.println("is Off");

        }
        return true;*/
        return isOn;
    }
}
