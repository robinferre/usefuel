package fr.esiea.ferre.usefuel;

/**
 * Created by Raphaël on 29/06/2017.
 */

public class OrderFuel {
    private String fuelQuantity;

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
