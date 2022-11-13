package projetoFinal.conectPet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetoFinal.conectPet.domain.entity.DoacaoEntity;

import java.util.Optional;


@Repository
public interface DoacoesRepository extends JpaRepository<DoacaoEntity, Integer> {
}
