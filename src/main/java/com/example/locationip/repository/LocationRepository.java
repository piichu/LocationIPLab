package com.example.locationip.repository;

import com.example.locationip.model.IP;
import com.example.locationip.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByIps(IP ip);
}
