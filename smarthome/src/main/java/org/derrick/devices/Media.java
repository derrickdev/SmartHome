package org.derrick.devices;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Media implements deviceController {
    private String name;
    private Boolean isOn ;

    public Media (String name) {
        this.name=name;
        this.isOn=false;
    }
    public void changeState (Boolean state) {
        this.isOn = state;
    }

    @Override
    public Boolean status() {

        return isOn;
    }
}
