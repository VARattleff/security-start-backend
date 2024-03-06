package dat3.recipe.repository;

import dat3.recipe.entity.SpecialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialUserRepository extends JpaRepository<SpecialUser, String> {
}
