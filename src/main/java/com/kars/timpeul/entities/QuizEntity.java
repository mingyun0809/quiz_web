package com.kars.timpeul.entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizEntity {
    private int index;
    private String titleText;
    private String infoText;
    private String madeToken;
    private boolean isAdminOnly;
}
