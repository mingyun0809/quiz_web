package com.kars.timpeul.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "listIndex")
public class ArticleQuestionEntity {
    private int listIndex;
    private int itemIndex;
    private String question;
    private String answer;
}
