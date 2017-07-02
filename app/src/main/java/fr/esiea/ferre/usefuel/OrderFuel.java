package fr.esiea.ferre.usefuel;

import android.location.Address;

/**
 * Created by Raphaël on 29/06/2017.
 */

public class OrderFuel {
    private String fuelQuantity;
    private Car car;
    private Address address;
    private String price;

    public OrderFuel(){}
    public OrderFuel(String p_fuelQuantity){
        fuelQuantity = p_fuelQuantity;
    }

    public String getFuelQuantity() {
        return fuelQuantity;
    }
    public void setFuelQuantity(String fuel) {
        this.fuelQuantity = fuel;
    }

}
