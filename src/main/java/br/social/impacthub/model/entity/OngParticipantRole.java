package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity @Table(name = "ong_participant_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OngParticipantRole {
    @Id @Column(name = "id")
    private Integer id;

    @NotNull
    private String name;

    @Getter
    public static enum Values {
        OWNER(1, "owner"),
        ADM(2, "adm"),
        MOD(3, "mod"),
        SPONSOR(4, "sponsor");

        private final Integer id;
        private final String name;

        private Values(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}