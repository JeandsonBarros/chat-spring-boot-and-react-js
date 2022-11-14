package com.chat.br.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeForgottenPasswordDto {

    @Email
    @NotBlank
    private String email;
    @NotNull
    private int code;
    @NotBlank
    private String newPassword;
}
