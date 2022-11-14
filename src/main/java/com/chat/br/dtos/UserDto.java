package com.chat.br.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
}
