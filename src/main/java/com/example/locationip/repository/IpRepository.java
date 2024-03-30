package com.example.locationip.repository;

import com.example.locationip.model.Ip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Ip repository. */
@Repository
public interface IpRepository extends JpaRepository<Ip, Long> {
  boolean existsByAddress(String address);

  Ip findByAddress(String address);

  Ip getIpById(Long id);
}
