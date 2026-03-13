package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ong_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OngCategory {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "ong_category_id")
    private UUID OngCategoryId;

    @NotNull
    private String name;
}
