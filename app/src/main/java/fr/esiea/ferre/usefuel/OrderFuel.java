package fr.esiea.ferre.usefuel;

import android.location.Address;

/**
 * Created by Raphaël on 29/06/2017.
 */

public class OrderFuel {
    private String fuelQuantity;
    private Car car;
    private String address;
    private double lat;
    private double lng;
    private String price;
    private String status;
    private String deliverer;
    private double latDeliv;
    private double lngDelive;

    public OrderFuel(){}
    public OrderFuel(String p_fuelQuantity, Car p_car, String p_address, double p_lat, double p_lng, String p_price, String p_status, String p_deliverer, double p_latDeliv, double p_lngDeliv){
        fuelQuantity = p_fuelQuantity;
        car = p_car;
        address = p_address;
        lat = p_lat;
        lng = p_lng;
        price = p_price;
        status = p_status;
        deliverer = p_deliverer;
        latDeliv = p_latDeliv;
        lngDelive = p_lngDeliv;
    }

    public String getFuelQuantity() {
        return fuelQuantity;
    }
    public void setFuelQuantity(String fuel) {
        this.fuelQuantity = fuel;
    }
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDeliverer() {
        return deliverer;
    }
    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }

    public double getLatDeliv() {
        return latDeliv;
    }
    public void setLatDeliv(double latDeliv) {
        this.latDeliv = latDeliv;
    }
    public double getLngDelive() {
        return lngDelive;
    }
    public void setLngDelive(double lngDelive) {
        this.lngDelive = lngDelive;
    }

}
