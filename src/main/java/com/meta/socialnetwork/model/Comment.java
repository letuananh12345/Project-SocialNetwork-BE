package com.meta.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private LocalDate created_date;
    private LocalDate modified_date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment rep_comment;
}
