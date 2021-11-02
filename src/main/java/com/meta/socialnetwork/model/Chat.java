package com.meta.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;
import java.util.TimeZone;

@Entity
@Data
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Boolean status;
    private TimeZone time_send;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_send;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user_receive;
}
