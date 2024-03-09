package com.example.locationip.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.List;

@Getter
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_generator")
    private Long id;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IP> ips;

    @ManyToMany()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "locatio_tags", joinColumns = @JoinColumn(name = "location_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private List<Tag> tags;
}