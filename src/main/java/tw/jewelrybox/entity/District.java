package tw.jewelrybox.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 行政區
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "District.findAll", query = "SELECT d FROM District d")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"District\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"name\""})
})
public class District implements java.io.Serializable {

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
	 * 行政區名稱
	 */
	@Basic(optional = false)
	@Column(name = "\"name\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String name;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof District)) {
			return false;
		}
		District other = (District) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.District[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public District() {
	}

	/**
	 * @param id 主鍵(長度16位元)
	 */
	public District(Short id) {
		this.id = id;
	}

	/**
	 * @param name 行政區名稱
	 */
	public District(String name) {
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
	 * @return 行政區名稱
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 行政區名稱
	 */
	public void setName(String name) {
		this.name = name;
	}
}
