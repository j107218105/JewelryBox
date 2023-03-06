package tw.jewelrybox.repository;

import java.util.Collection;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.*;

/**
 * 方式
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, CandidateRepositoryCustom {

	@Query(name = "Candidate.countByActivityAndRepliedTrueAndRepliedByNotNull")
	public long countByActivityAndRepliedTrueAndRepliedByNotNull(@Param("activity") Activity activity);

	/**
	 * @param activity 活動
	 * @return 被遴選進「活動」的「候選者」。
	 */
	public Collection<Candidate> findByActivity(Activity activity);

	@Query(name = "Candidate.findBySomeoneOrderBySince")
	public Page<Activity> findBySomeoneOrderBySince(@Param("someone") Someone someone, Pageable pageable);

	/**
	 * @param activity 活動
	 * @param someone 會員
	 * @return
	 */
	public Candidate findOneByActivityAndSomeone(@Param("activity") Activity activity, @Param("someone") Someone someone);
}
