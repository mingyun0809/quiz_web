package com.kars.timpeul.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"listIndex", "itemIndex"})
public class ArticleQuestionEntity {
    private int listIndex;
    private int itemIndex;
    private String question;
    private String answer;
}
