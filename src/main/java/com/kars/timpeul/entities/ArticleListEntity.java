package com.kars.timpeul.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class ArticleListEntity {
    private int index;
    private String title;
    private String info;
    private String token;
    private boolean isAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isDeleted;
}
