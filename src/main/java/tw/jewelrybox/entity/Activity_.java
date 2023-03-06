package tw.jewelrybox.entity;

import java.util.Date;
import javax.persistence.metamodel.*;

/**
 * 「活動」資料模型
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@StaticMetamodel(Activity.class)
public class Activity_ {

	/**
	 * 主鍵(長度32位元)
	 */
	public static volatile SingularAttribute<Activity, Integer> id;

	/**
	 * 活動名稱
	 */
	public static volatile SingularAttribute<Activity, String> name;

	/**
	 * 人數
	 */
	public static volatile SingularAttribute<Activity, Integer> headcount;

	/**
	 * 價值點數
	 */
	public static volatile SingularAttribute<Activity, Short> score;

	/**
	 * 已通知
	 */
	public static volatile SingularAttribute<Activity, Boolean> notified;

	/**
	 * 開始時戳
	 */
	public static volatile SingularAttribute<Activity, Date> since;

	/**
	 * 結束時戳
	 */
	public static volatile SingularAttribute<Activity, Date> until;

	/**
	 * 年齡需求
	 */
	public static volatile SingularAttribute<Activity, Short> age;

	/**
	 * 性別需求
	 */
	public static volatile SingularAttribute<Activity, Boolean> gender;

	/**
	 * 職業需求
	 */
	public static volatile SingularAttribute<Activity, Occupation> occupation;

	/**
	 * 行政區需求
	 */
	public static volatile SingularAttribute<Activity, District> district;
}
