package fr.esiea.ferre.usefuel;

/**
 * Created by rob on 6/2/2017.
 */

public class Car {

    private String numberPlate;
    private String brand;
    private String color;
    private String fuelType;
    /*private String tankCapacity;*/

    public Car(){}
    public Car(String p_numberPlate, String p_brand, String p_color, /*String p_tankCapacity,*/ String p_fuelType) {
        numberPlate = p_numberPlate;
        brand = p_brand;
        color = p_color;
        //tankCapacity = p_tankCapacity;
        fuelType = p_fuelType;
    }

    public String getNumberPlate() {
        return numberPlate;
    }
    public void setNumberPlate(String plate) {
        this.numberPlate = plate;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    /*public String getTankCapacity() {
        return tankCapacity;
    }
    public void setTankCapacity(String tankCapacity) {
        this.tankCapacity = tankCapacity;
    }*/

    public String getFuelType() {
        return fuelType;
    }
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }


}