package com.study.userity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDTO {
    private Long id;
    private String userName;
    private String fullName;
}
