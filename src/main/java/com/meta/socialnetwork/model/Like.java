package com.meta.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private  Post posts;
}
