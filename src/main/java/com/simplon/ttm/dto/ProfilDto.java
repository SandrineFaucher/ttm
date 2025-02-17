package com.simplon.ttm.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
    //id des sector
    private List<Long> sectors;
    //id des accompaniement
    private List<Long> accompaniements;
    private String content;
    private String city;
    private String department;
    private String region;
    private Long userId;
}
