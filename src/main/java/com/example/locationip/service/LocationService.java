package com.example.locationip.service;

import com.example.locationip.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    public Location getLocationByIPAddress(String ipAddress){
        if (ipAddress.equals("192.168.0.0")) {
            return new Location(ipAddress, "Belarus", "Minsk");
        } else if (ipAddress.equals("10.0.0.1")) {
            return new Location(ipAddress, "Russia", "Khabarovsk");
        } else {
            return new Location(ipAddress, "Belarus", "Baranovichi");
        }
    }
}