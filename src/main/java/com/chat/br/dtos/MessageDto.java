package com.chat.br.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageDto {
    @NotBlank
    private String text;
    @NotBlank
    @Email
    private String emailRecipient;

}
