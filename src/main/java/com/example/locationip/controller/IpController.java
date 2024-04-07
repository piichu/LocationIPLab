package com.example.locationip.controller;

import com.example.locationip.model.Ip;
import com.example.locationip.service.IpService;
import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** The type Ip controller. */
@RestController
@RequestMapping("/ips")
public class IpController {
  private final IpService ipService;

  @Autowired
  public IpController(IpService ipService) {
    this.ipService = ipService;
  }

  @GetMapping("/{id}")
  public Ip getIpById(@PathVariable Long id) {
    return ipService.getIpById(id);
  }

  @GetMapping
  public List<Ip> getAllIps() {
    return ipService.getAllIps();
  }

  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public Long createIp(@RequestParam String address, @RequestParam Long locationId) {
    ipService.createIp(address, locationId);
    return ipService.getIpByAddress(address).getId();
  }

  @Transactional
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{id}")
  public Long updateIp(
      @PathVariable Long id,
      @RequestParam(required = false) String address,
      @RequestParam(required = false) Long locationId) {
    ipService.updateIp(id, address, locationId);
    return id;
  }

  @Transactional
  @DeleteMapping("/{id}")
  public Long deleteIp(@PathVariable Long id) {
    ipService.deleteIpById(id);
    return id;
  }
}
