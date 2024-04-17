package com.example.locationip.controller;

import com.example.locationip.model.Ip;
import com.example.locationip.service.IpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IpControllerTest {
  @Mock private IpService ipService;

  @InjectMocks private IpController ipController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetIpById() {
    Long ipId = 1L;
    Ip ip = new Ip();
    ip.setId(ipId);
    when(ipService.getIpById(ipId)).thenReturn(ip);
    Ip result = ipController.getIpById(ipId);
    verify(ipService, times(1)).getIpById(ipId);
    assertEquals(ip, result);
  }

  @Test
  void testGetAllIps() {
    List<Ip> ips = Arrays.asList(new Ip(), new Ip());
    when(ipService.getAllIps()).thenReturn(ips);
    List<Ip> result = ipController.getAllIps();
    verify(ipService, times(1)).getAllIps();
    assertEquals(ips, result);
  }

  @Test
  void testCreateIp() {
    String address = "192.168.0.1";
    Long locationId = 1L;
    Long ipId = 1L;
    Ip ip = new Ip();
    ip.setId(ipId);
    ip.setAddress(address);
    when(ipService.getIpByAddress(address)).thenReturn(ip);
    Long result = ipController.createIp(address, locationId);
    verify(ipService, times(1)).createIp(address, locationId);
    verify(ipService, times(1)).getIpByAddress(address);
    assertEquals(ipId, result);
  }

  @Test
  void testUpdateIp() {
    Long ipId = 1L;
    String newAddress = "192.168.0.2";
    Long newLocationId = 2L;
    doNothing().when(ipService).updateIp(ipId, newAddress, newLocationId);
    Long result = ipController.updateIp(ipId, newAddress, newLocationId);
    verify(ipService, times(1)).updateIp(ipId, newAddress, newLocationId);
    assertEquals(ipId, result);
  }

  @Test
  void testDeleteIp() {
    Long ipId = 1L;
    Long result = ipController.deleteIp(ipId);
    verify(ipService, times(1)).deleteIpById(ipId);
    assertEquals(ipId, result);
  }
}
