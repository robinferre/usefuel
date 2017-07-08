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
    private String status;
    private String deliverer;

    public OrderFuel(){}
    public OrderFuel(String p_fuelQuantity, Car p_car, Address p_address, String p_price, String p_status, String p_deliverer){
        fuelQuantity = p_fuelQuantity;
        car = p_car;
        address = p_address;
        price = p_price;
        status = p_status;
        deliverer = p_deliverer;
    }

    public String getFuelQuantity() {
        return fuelQuantity;
    }
    public void setFuelQuantity(String fuel) {
        this.fuelQuantity = fuel;
    }

}
