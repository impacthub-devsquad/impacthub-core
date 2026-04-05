package br.social.impacthub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EventLikeId {
    private Ong ong;
    private UserProfile user;
}
