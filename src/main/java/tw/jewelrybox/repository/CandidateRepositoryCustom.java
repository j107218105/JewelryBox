package tw.jewelrybox.repository;

import java.util.Collection;
import tw.jewelrybox.entity.Activity;
import tw.jewelrybox.entity.Someone;

/**
 * 自訂「候選者」庫。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
public interface CandidateRepositoryCustom {

	public Collection<Activity> findUnrepliedActivities();

	/**
	 * @return 已證明但未審核的候選者會員。
	 */
	public Collection<Someone> findThoseWhoNeedApprovals();

	/**
	 * @return 未開始的「活動」。
	 */
	public Collection<Activity> findAvailableActivity();

	/**
	 * @param someone 會員
	 * @return 「會員」未開始的「活動」。
	 */
	public Collection<Activity> findAvailableActivity(Someone someone);

	/**
	 * @return 進行中的「活動」。
	 */
	public Collection<Activity> findUnderwayActivity();

	/**
	 * @param someone 會員
	 * @return 「會員」進行中的「活動」。
	 */
	public Collection<Activity> findUnderwayActivity(Someone someone);

	/**
	 * @return 已結束的「活動」。
	 */
	public Collection<Activity> findFinishedActivity();

	/**
	 * @param someone 會員
	 * @return 「會員」已結束的「活動」。
	 */
	public Collection<Activity> findFinishedActivity(Someone someone);
}
