package com.simplon.ttm.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter @Setter
public class Message {
    private String id;
    private User user1;
    private User user2;
    private String content;

}
