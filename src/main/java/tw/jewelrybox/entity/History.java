package tw.jewelrybox.entity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 歷程
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "History.findAll", query = "SELECT h FROM History h"),
	@NamedQuery(name = "History.findById", query = "SELECT h FROM History h WHERE h.id = :id"),
	@NamedQuery(name = "History.findByAmount", query = "SELECT h FROM History h WHERE h.amount = :amount"),
	@NamedQuery(name = "History.findByFinancialAccountHolder", query = "SELECT h FROM History h WHERE h.financialAccountHolder = :financialAccountHolder"),
	@NamedQuery(name = "History.findByFinancialAccountNumber", query = "SELECT h FROM History h WHERE h.financialAccountNumber = :financialAccountNumber"),
	@NamedQuery(name = "History.findByOccurred", query = "SELECT h FROM History h WHERE h.occurred = :occurred")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"History\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"activity\"", "\"someone\""}),
	@UniqueConstraint(columnNames = {"\"financialInstitution\"", "\"financialAccountNumber\""})
})
public class History implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵(長度64位元)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Basic(optional = false)
	private Long id;

	/**
	 * 活動
	 */
	@JoinColumn(name = "\"activity\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private Activity activity;

	/**
	 * 會員
	 */
	@JoinColumn(name = "\"someone\"", referencedColumnName = "\"id\"", nullable = false)
	@ManyToOne(optional = false)
	private Someone someone;

	/**
	 * 金額
	 */
	@Column(name = "amount")
	private Integer amount;

	/**
	 * 金融機構
	 */
	@JoinColumn(name = "\"financialInstitution\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private FinancialInstitution financialInstitution;

	/**
	 * 金融帳戶戶名
	 */
	@Column(name = "financialAccountHolder", length = 2147483647)
	@Size(max = 2147483647)
	private String financialAccountHolder;

	/**
	 * 金融帳戶號碼
	 */
	@Column(name = "financialAccountNumber", length = 2147483647)
	@Size(max = 2147483647)
	private String financialAccountNumber;

	/**
	 * 發生時戳
	 */
	@Column(name = "occurred")
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurred;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof History)) {
			return false;
		}
		History other = (History) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.History[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public History() {
	}

	/**
	 * @param id 主鍵(長度64位元)
	 */
	protected History(Long id) {
		this.id = id;
	}

	/**
	 * @return 主鍵(長度64位元)
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id 主鍵(長度64位元)
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 活動
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity 活動
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return 會員
	 */
	public Someone getSomeone() {
		return someone;
	}

	/**
	 * @param someone 會員
	 */
	public void setSomeone(Someone someone) {
		this.someone = someone;
	}

	/**
	 * @return 金額
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount 金額
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
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
	 * @return 發生時戳
	 */
	public Date getOccurred() {
		return occurred;
	}

	/**
	 * @param occurred 發生時戳
	 */
	public void setOccurred(Date occurred) {
		this.occurred = occurred;
	}
}
