package com.example.locationip.controller;

import com.example.locationip.model.IP;
import com.example.locationip.service.IPService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ips")
public class IPController {
    private final IPService ipService;

    public IPController(IPService ipService) {
        this.ipService = ipService;
    }

    @PostMapping
    public ResponseEntity<String> createIP(@RequestBody IP ip) {
        if (ipService.existsByAddress(ip.getAddress())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("IP with the given address already exists.");
        }
        IP newIP = new IP();
        newIP.setAddress(ip.getAddress());
        ipService.saveIP(ip);
        return ResponseEntity.status(HttpStatus.CREATED).body("IP created successfully.");
    }

    @GetMapping("/{id}")
    public IP getIPById(@PathVariable Long id) {
        return ipService.getIPById(id);
    }

    @GetMapping
    public List<IP> getAllIPs() {
        return ipService.getAllIPs();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIP(@PathVariable Long id, @RequestBody IP ip) {
        if (!ipService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Requested IP don't exist");
        }
        ip.setId(id);
        ipService.saveIP(ip);
        return ResponseEntity.ok("IP has been changed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIP(@PathVariable Long id) {
        if (!ipService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Requested IP don't exist");
        }
        ipService.deleteIP(id);
        return ResponseEntity.ok("IP has been deleted");
    }
}
