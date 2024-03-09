package com.example.locationip.service;

import com.example.locationip.model.IP;
import com.example.locationip.repository.IPRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPService {
    private final IPRepository ipRepository;

    public IPService(IPRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    public IP saveIP(IP ip) {
        return ipRepository.save(ip);
    }

    public IP getIPById(Long id) {
        return ipRepository.findById(id).orElse(null);
    }

    public IP getIPByAddress(String address) {
        return ipRepository.findByAddress(address);
    }

    public List<IP> getAllIPs() {
        return ipRepository.findAll();
    }

    public void deleteIP(Long id) {
        ipRepository.deleteById(id);
    }

    public boolean existsByAddress(String address) {
        return ipRepository.existsByAddress(address);
    }

    public boolean existsById(Long id) {
        return ipRepository.existsById(id);
    }
}
