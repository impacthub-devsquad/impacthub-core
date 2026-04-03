package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOngRequest(
        @NotBlank String name,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull String category
)
{
}
