package tw.jewelrybox.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import tw.jewelrybox.entity.*;

/**
 * 方式
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface SomeoneRepository extends org.springframework.data.jpa.repository.JpaRepository<Someone, Integer>, JpaSpecificationExecutor<Someone> {

	/**
	 * @param id 主鍵
	 * @param email 電子郵件
	 * @return 非「主鍵」但符合「電子郵件」者。
	 */
	public Long countByIdNotAndEmail(Integer id, String email);

	/**
	 * @param gender 性別
	 * @return 符合性別的數量。
	 */
	public Long countByGender(boolean gender);

	/**
	 * @param financialInstitution 金融機構
	 * @param financialAccountNumber 金融帳戶號碼
	 * @return 同時符合兩參數的數量。
	 */
	public Long countByFinancialInstitutionAndFinancialAccountNumber(FinancialInstitution financialInstitution, String financialAccountNumber);

	/**
	 * @param id 主鍵
	 * @param financialInstitution 金融機構
	 * @param financialAccountNumber 金融帳戶號碼
	 * @return 非「主鍵」但符合「金融機構」、「金融帳戶號碼」者。
	 */
	public Long countByIdNotAndFinancialInstitutionAndFinancialAccountNumber(Integer id, FinancialInstitution financialInstitution, String financialAccountNumber);

	/**
	 * @param facebookId Facebook帳號ID
	 * @return 會員
	 */
	public Someone findOneByFacebookId(String facebookId);

	/**
	 * @param googleID Google帳號ID
	 * @return 會員
	 */
	public Someone findOneByGoogleID(String googleID);

	/**
	 * @param email 電子郵件
	 * @return 會員
	 */
	public Someone findOneByEmail(String email);

	@Query(name = "Someone.findOneByIdAndDeniedFalse")
	public Someone findOneByIdAndDeniedFalse(@Param("id") Integer id);
}
