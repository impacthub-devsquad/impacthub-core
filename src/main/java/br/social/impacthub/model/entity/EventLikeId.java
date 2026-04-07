package br.social.impacthub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EventLikeId {
    private Event event;
    private UserProfile user;
}
