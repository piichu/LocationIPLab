package com.example.locationip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ips")
@Data
public class IP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address")
    private String address;

    @ManyToOne()
    @JsonIgnoreProperties({"ips","tags"})
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
