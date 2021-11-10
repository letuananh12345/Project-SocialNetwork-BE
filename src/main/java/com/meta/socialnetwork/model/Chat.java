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

    public Chat() {
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
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
    //    private Boolean status;
//    private TimeZone time_send;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user_send;
//
//    @ManyToOne
//    @JoinColumn(name = "users_id")
//    private User user_receive;

    @Override
    public String toString() {
        return "Chat{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
