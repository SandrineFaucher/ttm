package com.simplon.ttm.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String id;
    private User user1;
    private User user2;
    private String content;

}
