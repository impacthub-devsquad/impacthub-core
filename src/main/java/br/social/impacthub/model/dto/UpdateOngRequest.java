package br.social.impacthub.model.dto;

import java.util.Optional;

public record UpdateOngRequest(
        Optional <String> name,
        Optional <String> description,
        Optional <String> title
)
{
}
