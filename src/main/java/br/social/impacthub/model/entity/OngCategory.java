package br.social.impacthub.model.entity;

import br.social.impacthub.exception.InvalidOngCategoryException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ong_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OngCategory {
    @Id
    @Column(name = "id")
    private Integer ongCategoryId;

    @NotNull
    private String name;

    @Getter
    public static enum Values {
        EDUCATION(1, "education"),
        HEALTH(2, "health"),
        ENVIRONMENT(3, "environment"),
        ANIMAL_WELFARE(4, "animal_welfare"),
        HUMAN_RIGHTS(5, "human_rights"),
        POVERTY_ALLEVIATION(6, "poverty_alleviation"),
        ARTS_AND_CULTURE(7, "arts_and_culture"),
        SPORTS_AND_RECREATION(8, "sports_and_recreation");

        public final int id;
        public final String name;

        private Values(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Integer getIdByName(String category) {
            for (Values value : Values.values()) {
                if (value.name.equalsIgnoreCase(category)) {
                    return value.id;
                }
            }
            throw new InvalidOngCategoryException("Invalid category name: " + category);
        }
    }
}
