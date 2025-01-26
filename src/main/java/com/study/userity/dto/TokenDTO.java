package com.study.userity.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class TokenDTO {
    private String userName;
    private boolean authenticated;
    private Date createdAt;
    private Date expiration;
    private String accessToken;
    private String refreshToken;
}
