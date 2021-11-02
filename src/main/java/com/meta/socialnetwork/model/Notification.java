package com.meta.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notify;

    @OneToOne
    @JoinColumn(name = "like_id")
    private Like like;

    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
