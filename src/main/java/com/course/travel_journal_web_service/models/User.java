package com.course.travel_journal_web_service.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;
//    private String email;
    private String password;
    private String role;
//    private String image;
}
