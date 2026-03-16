package br.social.impacthub.model.dto;

public record UpdateUserRequest (
        String username,
        String email,
        String password,
        String description
){
}
