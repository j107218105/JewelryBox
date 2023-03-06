package tw.jewelrybox.entity;

import java.util.Date;
import javax.persistence.metamodel.*;

/**
 * 「會員」資料模型
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@StaticMetamodel(Someone.class)
public class Someone_ {

	/**
	 * 主鍵(長度32位元)
	 */
	public static volatile SingularAttribute<Someone, Integer> id;

	/**
	 * Facebook帳號
	 */
	public static volatile SingularAttribute<Someone, String> facebookId;

	/**
	 * Google帳號
	 */
	public static volatile SingularAttribute<Someone, String> googleID;

	/**
	 * 生日
	 */
	public static volatile SingularAttribute<Someone, Date> birthday;

	/**
	 * 性別
	 */
	public static volatile SingularAttribute<Someone, Boolean> gender;

	/**
	 * 全名
	 */
	public static volatile SingularAttribute<Someone, String> name;

	/**
	 * 電子郵件
	 */
	public static volatile SingularAttribute<Someone, String> email;

	/**
	 * 封鎖
	 */
	public static volatile SingularAttribute<Someone, Boolean> denied;

	/**
	 * 手機
	 */
	public static volatile SingularAttribute<Someone, String> cellular;

	/**
	 * 職業
	 */
	public static volatile SingularAttribute<Someone, Occupation> occupation;

	/**
	 * 金融機構
	 */
	public static volatile SingularAttribute<Someone, FinancialInstitution> financialInstitution;

	/**
	 * 金融帳戶戶名
	 */
	public static volatile SingularAttribute<Someone, String> financialAccountHolder;

	/**
	 * 金融帳戶號碼
	 */
	public static volatile SingularAttribute<Someone, String> financialAccountNumber;

	/**
	 * 行政區
	 */
	public static volatile SingularAttribute<Someone, District> district;
}
