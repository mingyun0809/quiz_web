package com.kars.timpeul.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "idToken")
public class UserEntity {
    private String idToken;
    private boolean isAdmin;
    private boolean isDeleted;
    private boolean isSuspended;
    private LocalDateTime createdAt;
}
