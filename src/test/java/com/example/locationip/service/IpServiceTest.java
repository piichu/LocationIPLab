package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.Ip;
import com.example.locationip.model.Location;
import com.example.locationip.repository.IpRepository;
import com.example.locationip.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class IpServiceTest {
  @Mock private IpRepository ipRepository;

  @Mock private LocationRepository locationRepository;
  @InjectMocks private IpService ipService;

  @Mock private Cache cache;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetIpById_CacheMiss() {
    Long id = 123L;
    Ip ip = new Ip();
    ip.setAddress("1.1.1.1");
    ip.setId(id);
    when(cache.containsKey("ip-" + id)).thenReturn(false);
    when(ipRepository.findById(id)).thenReturn(java.util.Optional.of(ip));
    Ip result = ipService.getIpById(id);
    verify(cache, times(1)).containsKey("ip-" + id);
    verify(cache, times(1)).putToCache("ip-" + id, ip);
    verify(cache, never()).getFromCache(anyString());
    verify(ipRepository, times(1)).findById(id);
    assertEquals(ip, result);
  }

  @Test
  void testGetIpByAddress() {
    String address = "192.168.0.1";
    Ip ip = new Ip();
    ip.setAddress(address);
    ip.setId(1L);
    when(ipRepository.findByAddress(address)).thenReturn(ip);
    Ip result = ipService.getIpByAddress(address);
    verify(ipRepository, times(1)).findByAddress(address);
    assertEquals(ip, result);
  }

  @Test
  void testGetAllIps() {
    List<Ip> ips = Arrays.asList(new Ip(), new Ip(), new Ip());
    ips.get(0).setId(1L);
    ips.get(0).setAddress("192.168.0.1");
    ips.get(1).setId(2L);
    ips.get(1).setAddress("192.168.0.2");
    ips.get(2).setId(3L);
    ips.get(2).setAddress("192.168.0.3");
    when(ipRepository.findAll()).thenReturn(ips);
    List<Ip> result = ipService.getAllIps();
    verify(ipRepository, times(1)).findAll();
    assertEquals(ips, result);
  }

  @Test
  void testCreateIp() {
    String address = "192.168.0.1";
    Long locationId = 1L;
    Location location = new Location();
    location.setId(locationId);
    when(ipRepository.findByAddress(address)).thenReturn(null);
    when(locationRepository.getLocationById(locationId)).thenReturn(location);
    ipService.createIp(address, locationId);
    verify(ipRepository, times(1)).findByAddress(address);
    verify(ipRepository, times(1)).save(any(Ip.class));
    verify(locationRepository, times(1)).getLocationById(locationId);
    verify(cache, times(1)).putToCache(anyString(), any(Ip.class));
  }

  @Test
  void testCreateIpExistingIp() {
    String address = "192.168.0.1";
    Long locationId = 1L;
    Ip ip = new Ip();
    ip.setAddress(address);
    when(ipRepository.findByAddress(address)).thenReturn(ip);
    assertThrows(ResponseStatusException.class, () -> ipService.createIp(address, locationId));
    verify(ipRepository, times(1)).findByAddress(address);
    verifyNoMoreInteractions(ipRepository);
    verifyNoInteractions(locationRepository);
    verifyNoInteractions(cache);
  }

  @Test
  void testUpdateIp() {
    Long id = 1L;
    String address = "192.168.0.1";
    Long locationId = 1L;
    Location location = new Location();
    location.setId(locationId);
    Ip ip = new Ip();
    ip.setId(id);
    ip.setAddress(address);
    when(cache.containsKey("ip-" + id)).thenReturn(true);
    when(cache.getFromCache("ip-" + id)).thenReturn(ip);
    when(locationRepository.getLocationById(locationId)).thenReturn(location);
    ipService.updateIp(id, address, locationId);
    verify(cache, times(1)).containsKey("ip-" + id);
    verify(cache, times(1)).getFromCache("ip-" + id);
    verify(ipRepository, never()).getIpById(id);
    verify(ipRepository, times(1)).save(ip);
    verify(cache, times(1)).putToCache("ip-" + id, ip);
  }

  @Test
  void testUpdateIpWithoutCache() {
    Long id = 1L;
    String address = "192.168.0.1";
    Long locationId = 1L;
    Location location = new Location();
    location.setId(locationId);
    Ip ip = new Ip();
    ip.setId(id);
    ip.setAddress(address);
    when(cache.containsKey("ip-" + id)).thenReturn(false);
    when(ipRepository.getIpById(id)).thenReturn(ip);
    when(locationRepository.getLocationById(locationId)).thenReturn(location);
    ipService.updateIp(id, address, locationId);
    verify(cache, times(1)).containsKey("ip-" + id);
    verify(cache, never()).getFromCache("ip-" + id);
    verify(ipRepository, times(1)).getIpById(id);
    verify(ipRepository, times(1)).save(ip);
    verify(cache, times(1)).putToCache("ip-" + id, ip);
  }

  @Test
  void testDeleteIpById() {
    Long id = 1L;
    ipService.deleteIpById(id);
    verify(ipRepository, times(1)).deleteById(id);
    verify(cache, times(1)).removeFromCache("ip-" + id);
  }
}
