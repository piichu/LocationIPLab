package com.example.locationip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IP {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ip_generator")
    private Long id;
    @Column(name = "address")
    private String address;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "locationId")
    private Location location;
}