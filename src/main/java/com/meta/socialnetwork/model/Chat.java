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
    private String name;
    private String message;
    private int img;
//

    public Chat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
    //    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user_send;
//
//    @ManyToOne
//    @JoinColumn(name = "users_id")
//    private User user_receive;
}
