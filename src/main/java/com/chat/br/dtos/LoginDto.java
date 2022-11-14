package com.chat.br.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
