package co.istad.iteecomerc.feature.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest (
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String confirmedPassword,
        @NotBlank
        String email,
        @NotBlank
        String firstName,
        @NotBlank

        String lastName,
        @NotBlank
        String phoneNumber
){
}
