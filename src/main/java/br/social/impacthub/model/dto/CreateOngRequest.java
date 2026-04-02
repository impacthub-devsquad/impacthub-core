package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOngRequest(
        @NotBlank String name,
        @NotBlank String title,
        @NotBlank String description
)
{
}
