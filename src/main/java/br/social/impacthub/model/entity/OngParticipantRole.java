package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

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
    public static enum Value {
        OWNER(1, "owner"),
        ADM(2, "adm"),
        MOD(3, "mod"),
        SPONSOR(4, "sponsor");

        private final Integer id;
        private final String name;

        private Value(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Optional<Value> fromName(String name){
            return switch (name){
                 case "owner" -> Optional.of(OWNER);
                 case "adm" -> Optional.of(ADM);
                 case "mod" -> Optional.of(MOD);
                 case "sponsor" -> Optional.of(SPONSOR);
                 default -> Optional.empty();
            };
        }
    }
}