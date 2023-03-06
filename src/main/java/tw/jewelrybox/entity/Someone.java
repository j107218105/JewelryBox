package tw.jewelrybox.entity;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 會員
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Someone.findAll", query = "SELECT s FROM Someone s"),
	@NamedQuery(name = "Someone.findOneByIdAndDeniedFalse", query = "SELECT s FROM Someone s WHERE s.id = :id AND s.denied = FALSE")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"Someone\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"facebookId\""}),
	@UniqueConstraint(columnNames = {"\"googleID\""}),
	@UniqueConstraint(columnNames = {"\"email\""}),
	@UniqueConstraint(columnNames = {"\"cellular\""}),
	@UniqueConstraint(columnNames = {"\"financialInstitution\"", "\"financialAccountNumber\""})
})
public class Someone implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵(長度32位元)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"id\"", nullable = false)
	@Basic(optional = false)
	private Integer id;

	/**
	 * Facebook帳號
	 */
	@Column(name = "\"facebookId\"", length = 2147483647)
	@Size(max = 2147483647)
	private String facebookId;

	/**
	 * Google帳號
	 */
	@Column(name = "\"googleID\"", length = 2147483647)
	@Size(max = 2147483647)
	private String googleID;

	/**
	 * 生日
	 */
	@Basic(optional = false)
	@Column(name = "\"birthday\"", nullable = false)
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date birthday;

	/**
	 * 性別
	 */
	@Basic(optional = false)
	@Column(name = "\"gender\"", nullable = false)
	@NotNull
	private boolean gender;

	/**
	 * 全名
	 */
	@Basic(optional = false)
	@Column(name = "\"name\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String name;

	/**
	 * 電子郵件
	 */
	@Basic(optional = false)
	@Column(name = "\"email\"", nullable = false, length = 2147483647)
	@NotNull
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email")//if the field contains email address consider using this annotation to enforce field validation
	@Size(min = 1, max = 2147483647)
	private String email;

	/**
	 * 封鎖
	 */
	@Basic(optional = false)
	@Column(name = "\"denied\"", nullable = false)
	@NotNull
	private boolean denied;

	/**
	 * 手機
	 */
	@Column(name = "\"cellular\"", length = 2147483647)
	@Size(max = 2147483647)
	private String cellular;

	/**
	 * 職業
	 */
	@JoinColumn(name = "\"occupation\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private Occupation occupation;

	/**
	 * 金融機構
	 */
	@JoinColumn(name = "\"financialInstitution\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private FinancialInstitution financialInstitution;

	/**
	 * 金融帳戶戶名
	 */
	@Column(name = "\"financialAccountHolder\"", length = 2147483647)
	@Size(max = 2147483647)
	private String financialAccountHolder;

	/**
	 * 金融帳戶號碼
	 */
	@Column(name = "\"financialAccountNumber\"", length = 2147483647)
	@Size(max = 2147483647)
	private String financialAccountNumber;

	/**
	 * 行政區
	 */
	@JoinColumn(name = "\"district\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private District district;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Someone)) {
			return false;
		}
		Someone other = (Someone) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.Someone1[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public Someone() {
		this.denied = false;
	}

	/**
	 * @param id 主鍵(長度32位元)
	 */
	protected Someone(Integer id) {
		this.id = id;
		this.denied = false;
	}

	/**
	 * @param birthday 生日
	 * @param gender 性別
	 * @param name 全名
	 * @param email 電子郵件
	 * @param denied 封鎖
	 */
	public Someone(Date birthday, boolean gender, String name, String email, boolean denied) {
		this.birthday = birthday;
		this.gender = gender;
		this.name = name;
		this.email = email;
		this.denied = denied;
	}

	/**
	 * @return 主鍵(長度32位元)
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id 主鍵(長度32位元)
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Facebook帳號
	 */
	public String getFacebookId() {
		return facebookId;
	}

	/**
	 * @param facebookId Facebook帳號
	 */
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	/**
	 * @return Google帳號
	 */
	public String getGoogleID() {
		return googleID;
	}

	/**
	 * @param googleID Google帳號
	 */
	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}

	/**
	 * @return 生日
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday 生日
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return 性別
	 */
	public boolean getGender() {
		return gender;
	}

	/**
	 * @param gender 性別
	 */
	public void setGender(boolean gender) {
		this.gender = gender;
	}

	/**
	 * @return 全名
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 全名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 電子郵件
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email 電子郵件
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return 封鎖
	 */
	public boolean isDenied() {
		return denied;
	}

	/**
	 * @param denied 封鎖
	 */
	public void setDenied(boolean denied) {
		this.denied = denied;
	}

	/**
	 * @return 手機
	 */
	public String getCellular() {
		return cellular;
	}

	/**
	 * @param cellular 手機
	 */
	public void setCellular(String cellular) {
		this.cellular = cellular;
	}

	/**
	 * @return 職業
	 */
	public Occupation getOccupation() {
		return occupation;
	}

	/**
	 * @param occupation 職業
	 */
	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	/**
	 * @return 金融機構
	 */
	public FinancialInstitution getFinancialInstitution() {
		return financialInstitution;
	}

	/**
	 * @param financialInstitution 金融機構
	 */
	public void setFinancialInstitution(FinancialInstitution financialInstitution) {
		this.financialInstitution = financialInstitution;
	}

	/**
	 * @return 金融帳戶戶名
	 */
	public String getFinancialAccountHolder() {
		return financialAccountHolder;
	}

	/**
	 * @param financialAccountHolder 金融帳戶戶名
	 */
	public void setFinancialAccountHolder(String financialAccountHolder) {
		this.financialAccountHolder = financialAccountHolder;
	}

	/**
	 * @return 金融帳戶號碼
	 */
	public String getFinancialAccountNumber() {
		return financialAccountNumber;
	}

	/**
	 * @param financialAccountNumber 金融帳戶號碼
	 */
	public void setFinancialAccountNumber(String financialAccountNumber) {
		this.financialAccountNumber = financialAccountNumber;
	}

	/**
	 * @return 行政區
	 */
	public District getDistrict() {
		return district;
	}

	/**
	 * @param district 行政區
	 */
	public void setDistrict(District district) {
		this.district = district;
	}
}
