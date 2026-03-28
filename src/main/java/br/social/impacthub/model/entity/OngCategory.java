package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
}
