package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.IP;
import com.example.locationip.repository.IPRepository;
import com.example.locationip.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IPService {
    private final IPRepository ipRepository;
    private final LocationRepository locationRepository;
    private final Cache cache;
    private static final String CACHE_KEY = "ip-";

    @Autowired
    IPService(IPRepository ipRepository, LocationRepository locationRepository, Cache cache) {
        this.ipRepository = ipRepository;
        this.locationRepository = locationRepository;
        this.cache = cache;
    }

    public IP getIPById(Long id) {
        if(cache.containsKey(CACHE_KEY+id)){
            return (IP)cache.getFromCache(CACHE_KEY+id);
        }
        IP ip= ipRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        cache.putToCache(CACHE_KEY+id, ip);
        return ip;
    }

    public List<IP> getAllIPs() {
        return ipRepository.findAll();
    }

    public void createIP(String address, Long locationId) {
        IP ip = ipRepository.findByAddress(address);
        if (ip == null) {
            ip = new IP();
            ip.setAddress(address);
            ip.setLocation(locationRepository.getLocationById(locationId));
            ipRepository.save(ip);
            cache.putToCache(CACHE_KEY+ip.getId(), ip);
        } else throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    public void updateIP(Long id, String address, Long locationId) {
        IP ip;
        if(cache.containsKey(CACHE_KEY+id)){
            ip=(IP) cache.getFromCache(CACHE_KEY+id);
        }else{
            ip=ipRepository.getIPById(id);
        }
        if (address != null) ip.setAddress(address);
        if (locationId != null) ip.setLocation(locationRepository.getLocationById(locationId));
        ipRepository.save(ip);
        cache.putToCache(CACHE_KEY+id,ip);
    }

    public void deleteIPById(Long id) {
        ipRepository.deleteById(id);
        cache.removeFromCache("ip-"+id);
    }
}
