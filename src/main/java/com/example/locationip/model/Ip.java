package com.example.locationip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/** The type Ip. */
@Entity
@Table(name = "ips")
@Data
public class Ip {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "address")
  private String address;

  @ManyToOne()
  @JsonIgnoreProperties({"ips", "tags"})
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;
}
