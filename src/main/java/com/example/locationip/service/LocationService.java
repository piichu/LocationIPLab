package com.example.locationip.service;

import com.example.locationip.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    public Location getLocationByIPAddress(String ipAddress){
        if (ipAddress.equals("255.255.255.255")) {
            return new Location(ipAddress, "Belarus", "Minsk");
        } else if (ipAddress.equals("0.0.0.0")) {
            return new Location(ipAddress, "Russia", "Khabarovsk");
        } else {
            return new Location(ipAddress, "Belarus", "Baranovichi");
        }
    }
}