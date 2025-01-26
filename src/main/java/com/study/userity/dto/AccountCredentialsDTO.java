package com.study.userity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
@Data
public class AccountCredentialsDTO {
    private String userName;
    private String password;
}
