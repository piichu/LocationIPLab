package com.example.locationip.repository;

import com.example.locationip.model.IP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPRepository extends JpaRepository<IP, Long> {
    boolean existsByAddress(String address);

    IP findByAddress(String address);

    IP getIPById(Long id);
}
