package br.social.impacthub.model.dto;

public record RegisterUserRequest(
        String name,
        String email,
        String password
)
{
}
