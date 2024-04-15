package com.example.locationip.repository;

import java.util.List;

/**
 * The type Location dto.
 */
public record LocationDto(String country, String city, List<Long> tags){}
