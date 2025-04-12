package com.course.travel_journal_web_service.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User author;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String location;

    @Column(columnDefinition="TEXT", length = 2000)
    private String description;

    @Column(nullable = false)
    private String imageName = "default-post-img.png";

    @Column(nullable = false)
    private Long likes = 0L;

    @Transient // Это поле не будет сохраняться в БД, так как оно вычисляемое
    private Boolean isLiked;

    @Transient // Это поле не будет сохраняться в БД
    private String image;
}
