package com.kars.timpeul.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class QuizContentEntity {
    private int itemIndex;
    private int listIndex;
    private String userToken;
    private String title;
    private String info;
    private String question;
    private String answers;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isDeleted;

}
