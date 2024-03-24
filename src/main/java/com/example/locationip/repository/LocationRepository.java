package com.example.locationip.repository;

import com.example.locationip.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l JOIN l.tags t WHERE t.name = :name")
    List<Location> findLocationByTag(@Param("name") String name);

    @Query("SELECT l FROM Location l JOIN l.ips i WHERE i.address = :address")
    Location findLocationByIP(@Param("address") String address);

    Location getLocationById(Long locationId);
}
