package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.Ip;
import com.example.locationip.repository.IpRepository;
import com.example.locationip.repository.LocationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** The type Ip service. */
@Service
public class IpService {
  private static final String CACHE_KEY = "ip-";
  private final IpRepository ipRepository;
  private final LocationRepository locationRepository;
  private final Cache cache;

  @Autowired
  IpService(IpRepository ipRepository, LocationRepository locationRepository, Cache cache) {
    this.ipRepository = ipRepository;
    this.locationRepository = locationRepository;
    this.cache = cache;
  }

  /**
   * Gets ip by id.
   *
   * @param id the id
   * @return the ip by id
   */
  public Ip getIpById(Long id) {
    if (cache.containsKey(CACHE_KEY + id)) {
      return (Ip) cache.getFromCache(CACHE_KEY + id);
    }
    Ip ip =
        ipRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    cache.putToCache(CACHE_KEY + id, ip);
    return ip;
  }

  public Ip getIpByAddress(String address) {
    return ipRepository.findByAddress(address);
  }

  public List<Ip> getAllIps() {
    return ipRepository.findAll();
  }

  /**
   * Create ip.
   *
   * @param address the address
   * @param locationId the location id
   */
  public void createIp(String address, Long locationId) {
    Ip ip = ipRepository.findByAddress(address);
    if (ip == null) {
      ip = new Ip();
      ip.setAddress(address);
      ip.setLocation(locationRepository.getLocationById(locationId));
      ipRepository.save(ip);
      cache.putToCache(CACHE_KEY + ip.getId(), ip);
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Update ip.
   *
   * @param id the id
   * @param address the address
   * @param locationId the location id
   */
  public void updateIp(Long id, String address, Long locationId) {
    Ip ip;
    if (cache.containsKey(CACHE_KEY + id)) {
      ip = (Ip) cache.getFromCache(CACHE_KEY + id);
    } else {
      ip = ipRepository.getIpById(id);
    }
    if (ip != null) {
      if (address != null) {
        ip.setAddress(address);
      }
      if (locationId != null) {
        ip.setLocation(locationRepository.getLocationById(locationId));
      }
      ipRepository.save(ip);
      cache.putToCache(CACHE_KEY + id, ip);
    }
  }

  public void deleteIpById(Long id) {
    ipRepository.deleteById(id);
    cache.removeFromCache(CACHE_KEY + id);
  }
}
