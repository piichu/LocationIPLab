package com.example.locationip;
public class Location {

    private String ipAddress;
    private String country;
    private String city;
    public Location(String ipAddress, String country, String city) {
        this.ipAddress = ipAddress;
        this.country = country;
        this.city = city;
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}