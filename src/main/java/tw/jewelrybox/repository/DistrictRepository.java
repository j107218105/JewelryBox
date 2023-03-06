package tw.jewelrybox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.District;

/**
 * 行政區
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface DistrictRepository extends JpaRepository<District, Short> {
}
