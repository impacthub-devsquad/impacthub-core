package br.social.impacthub.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PagedResponse<T> (
        Integer page,
        Integer size,
        Integer totalPages,
        Long totalElements,
        boolean isLast,
        List<T> content
){
}
