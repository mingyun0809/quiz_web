package com.kars.timpeul.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizEntity {
    private int index;
    private String title;
    private String info;
    private String question;
    private String answer;
}
