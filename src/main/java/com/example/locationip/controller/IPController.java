package com.example.locationip.controller;

import com.example.locationip.model.IP;
import com.example.locationip.service.IPService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ips")
public class IPController {
    private final IPService ipService;

    @Autowired
    public IPController(IPService ipService) {
        this.ipService = ipService;
    }

    @GetMapping("/{id}")
    public IP getIPById(@PathVariable Long id) {
        return ipService.getIPById(id);
    }

    @GetMapping
    public List<IP> getAllIPs() {
        return ipService.getAllIPs();
    }

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createIP(@RequestParam String address, @RequestParam Long locationId) {
        ipService.createIP(address, locationId);
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateIP(@PathVariable Long id,
                         @RequestParam(required = false) String address,
                         @RequestParam(required = false) Long locationId) {
        ipService.updateIP(id, address, locationId);
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteIP(@PathVariable Long id) {
        ipService.deleteIPById(id);
    }
}
