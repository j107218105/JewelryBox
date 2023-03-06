package tw.jewelrybox.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 途徑
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Mapping.findAll", query = "SELECT m FROM Mapping m"),
	@NamedQuery(name = "Mapping.findByAccordionOrderBySort", query = "SELECT m FROM Mapping m WHERE m.accordion = :accordion ORDER BY m.sort"),
	@NamedQuery(name = "Mapping.findByAccordionAndUriNotNullOrderBySort", query = "SELECT m FROM Mapping m WHERE m.accordion = :accordion AND m.uri IS NOT NULL ORDER BY m.sort")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"Mapping\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"uri\"", "\"method\""}),
	@UniqueConstraint(columnNames = {"\"accordion\"", "\"sort\""})
})
public class Mapping implements java.io.Serializable {

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
	 * 手風琴
	 */
	@JoinColumn(name = "\"accordion\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private Accordion accordion;

	/**
	 * 排序
	 */
	@Column(name = "\"sort\"")
	private Short sort;

	/**
	 * 標題
	 */
	@Column(name = "\"title\"", length = 2147483647)
	@Size(max = 2147483647)
	private String title;

	/**
	 * 路徑
	 */
	@Basic(optional = false)
	@Column(name = "\"uri\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String uri;

	/**
	 * 方式
	 */
	@JoinColumn(name = "\"method\"", nullable = false, referencedColumnName = "\"id\"")
	@ManyToOne(optional = false)
	private Method method;

	/**
	 * 規則
	 */
	@Basic(optional = false)
	@Column(name = "\"pattern\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String pattern;

	/**
	 * 描述
	 */
	@Basic(optional = false)
	@Column(name = "\"description\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String description;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Mapping)) {
			return false;
		}
		Mapping other = (Mapping) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.Mapping[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public Mapping() {
	}

	/**
	 * @param id 主鍵(長度16位元)
	 */
	protected Mapping(Short id) {
		this.id = id;
	}

	/**
	 * @param uri
	 */
	public Mapping(String uri) {
		this.uri = uri;
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
	 * @return 手風琴
	 */
	public Accordion getAccordion() {
		return accordion;
	}

	/**
	 * @param accordion 手風琴
	 */
	public void setAccordion(Accordion accordion) {
		this.accordion = accordion;
	}

	/**
	 * @return 排序
	 */
	public Short getSort() {
		return sort;
	}

	/**
	 * @param sort 排序
	 */
	public void setSort(Short sort) {
		this.sort = sort;
	}

	/**
	 * @return 標題
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title 標題
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return 路徑
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri 路徑
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return 方式
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @param method 方式
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * @return 規則
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern 規則
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description 描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
