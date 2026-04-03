package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OngCategoryRepository extends JpaRepository<OngCategory,Integer> {
    Optional<OngCategory> findOngCategoryByName(String name);
}
