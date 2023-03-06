package tw.jewelrybox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.Activity;

/**
 * 活動
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {
}
