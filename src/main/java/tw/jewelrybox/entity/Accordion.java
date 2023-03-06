package tw.jewelrybox.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 手風琴
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Accordion.findAll", query = "SELECT a FROM Accordion a"),
	@NamedQuery(name = "Accordion.findAllOrderBySort", query = "SELECT a FROM Accordion a ORDER BY a.sort")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"Accordion\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"name\""}),
	@UniqueConstraint(columnNames = {"\"sort\""})
})
public class Accordion implements java.io.Serializable {

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
	 * 手風琴名稱
	 */
	@Basic(optional = false)
	@Column(name = "\"name\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String name;

	/**
	 * 排序
	 */
	@Column(name = "\"sort\"", nullable = false)
	private short sort;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Accordion)) {
			return false;
		}
		Accordion other = (Accordion) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.Accordion[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public Accordion() {
	}

	/**
	 * @param id 主鍵(長度16位元)
	 */
	protected Accordion(Short id) {
		this.id = id;
	}

	/**
	 * @param name 手風琴名稱
	 */
	public Accordion(String name) {
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
	 * @return 手風琴名稱
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 手風琴名稱
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 排序
	 */
	public short getSort() {
		return sort;
	}

	/**
	 * @param sort 排序
	 */
	public void setSort(short sort) {
		this.sort = sort;
	}
}
