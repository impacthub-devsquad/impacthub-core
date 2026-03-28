package br.social.impacthub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class OngFollowerId {
    private Ong ong;
    private UserProfile user;
}
