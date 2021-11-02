package com.meta.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
