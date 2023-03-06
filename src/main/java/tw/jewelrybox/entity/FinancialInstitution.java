package tw.jewelrybox.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 金融機構
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "FinancialInstitution.findAll", query = "SELECT f FROM FinancialInstitution f"),
	@NamedQuery(name = "FinancialInstitution.findByShownTrueOrderByCode", query = "SELECT f FROM FinancialInstitution f WHERE f.shown = TRUE ORDER BY f.code")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"FinancialInstitution\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"name\""})
})
public class FinancialInstitution implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵(長度16位元)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"id\"", nullable = false)
	@Basic(optional = false)
	private Short id;

	/**
	 * 代碼
	 */
	@Column(name = "\"code\"", nullable = false, length = 2147483647)
	@Size(max = 2147483647)
	private String code;

	/**
	 * 金融機構名稱
	 */
	@Basic(optional = false)
	@Column(name = "\"name\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String name;

	/**
	 * 顯示與否
	 */
	@Basic(optional = false)
	@Column(name = "\"shown\"", nullable = false)
	@NotNull
	private boolean shown;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof FinancialInstitution)) {
			return false;
		}
		FinancialInstitution other = (FinancialInstitution) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.FinancialInstitution[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public FinancialInstitution() {
	}

	/**
	 * @param id 主鍵(長度16位元)
	 */
	protected FinancialInstitution(Short id) {
		this.id = id;
	}

	/**
	 * @param name 金融機構名稱
	 */
	public FinancialInstitution(String name) {
		this.name = name;
	}

	/**
	 * @return 主鍵(長度16位元)
	 */
	public Short getId() {
		return id;
	}

	/**
	 * @param id 主鍵(長度16位元)
	 */
	public void setId(Short id) {
		this.id = id;
	}

	/**
	 * @return 代碼
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code 代碼
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return 金融機構名稱
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 金融機構名稱
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 顯示與否
	 */
	public boolean isShown() {
		return shown;
	}

	/**
	 * @param shown 顯示與否
	 */
	public void setShown(boolean shown) {
		this.shown = shown;
	}
}
