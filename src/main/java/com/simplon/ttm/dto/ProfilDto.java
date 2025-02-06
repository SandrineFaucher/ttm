package com.simplon.ttm.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilDto {

    private String availability;
    private List<String> sectorIds;
    private List<String> accompaniementIds;
    private String content;
    private String city;
    private String department;
    private String region;
    private String image;
    private LocalDateTime createdAt;
    private Long userId;
}
