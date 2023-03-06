package tw.jewelrybox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.Occupation;

/**
 * 職業
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Short> {
}
