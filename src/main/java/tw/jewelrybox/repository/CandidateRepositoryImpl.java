package tw.jewelrybox.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import tw.jewelrybox.entity.Activity;
import tw.jewelrybox.entity.Someone;

/**
 * 實作「自訂候選者庫」。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@SuppressWarnings("JPQLValidation")
public class CandidateRepositoryImpl implements CandidateRepositoryCustom {

	@Autowired
	EntityManager entityManager;

	@Override
	public Collection<Activity> findUnrepliedActivities() {
		return entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE CURRENT_TIMESTAMP < a.since AND c.replied IS NULL ORDER BY a.since DESC", Activity.class).getResultList();
	}

	@Override
	public Collection<Someone> findThoseWhoNeedApprovals() {
		return entityManager.createQuery("SELECT DISTINCT c.someone FROM Candidate c LEFT JOIN c.activity a WHERE CURRENT_TIMESTAMP < a.since AND c.replied IS NULL ORDER BY a.since DESC", Someone.class).getResultList();
	}

	@Override
	public Collection<Activity> findAvailableActivity() {
		return entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE CURRENT_TIMESTAMP < a.since ORDER BY a.since DESC", Activity.class).getResultList();
	}

	@Override
	public Collection<Activity> findAvailableActivity(Someone someone) {
		TypedQuery<Activity> typedQuery = entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE c.someone = :someone AND CURRENT_TIMESTAMP < a.since AND c.replied IS NULL AND c.repliedBy IS NULL AND c.notified IS NULL ORDER BY a.since DESC", Activity.class);
		typedQuery.setParameter("someone", someone);
		return typedQuery.getResultList();
	}

	@Override
	public Collection<Activity> findUnderwayActivity() {
		return entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE CURRENT_TIMESTAMP >= a.since ORDER BY a.since DESC", Activity.class).getResultList();
	}

	@Override
	public Collection<Activity> findUnderwayActivity(Someone someone) {
		//TypedQuery<Activity> typedQuery = entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE c.someone = :someone AND CURRENT_TIMESTAMP < a.until AND c.replied = TRUE AND c.repliedBy IS NOT NULL AND c.notified = TRUE ORDER BY a.since DESC", Activity.class);
		//由於尚未實作簡訊發送所以...
		TypedQuery<Activity> typedQuery = entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE c.someone = :someone AND CURRENT_TIMESTAMP < a.until AND c.replied = TRUE AND c.repliedBy IS NOT NULL ORDER BY a.since DESC", Activity.class);
		typedQuery.setParameter("someone", someone);
		return typedQuery.getResultList();
	}

	@Override
	public Collection<Activity> findFinishedActivity() {
		return entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE CURRENT_TIMESTAMP >= a.until ORDER BY a.since DESC", Activity.class).getResultList();
	}

	@Override
	public Collection<Activity> findFinishedActivity(Someone someone) {
		TypedQuery<Activity> typedQuery = entityManager.createQuery("SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE c.someone = :someone AND CURRENT_TIMESTAMP >= a.until ORDER BY a.since DESC", Activity.class);
		typedQuery.setParameter("someone", someone);
		return typedQuery.getResultList();
	}
}
