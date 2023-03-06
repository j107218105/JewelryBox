package tw.jewelrybox.entity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 反饋
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"Feedback\"")
@NamedQueries({
	@NamedQuery(name = "Feedback.findAll", query = "SELECT f FROM Feedback f")
})
public class Feedback implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵(長度64位元)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"id\"", nullable = false)
	@Basic(optional = false)
	private Long id;

	/**
	 * 會員
	 */
	@JoinColumn(name = "\"someone\"", referencedColumnName = "\"id\"", nullable = false)
	@ManyToOne(optional = false)
	private Someone someone;

	/**
	 * 內容
	 */
	@Basic(optional = false)
	@Column(name = "\"context\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String context;

	/**
	 * 發文時戳
	 */
	@Basic(optional = false)
	@Column(name = "\"posted\"", nullable = false)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date posted;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Feedback)) {
			return false;
		}
		Feedback other = (Feedback) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.Feedback[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public Feedback() {
	}

	/**
	 * @param id 主鍵(長度64位元)
	 */
	protected Feedback(Long id) {
		this.id = id;
	}

	/**
	 * @param someone 會員
	 * @param context 內容
	 * @param posted 發文時戳
	 */
	public Feedback(Someone someone, String context, Date posted) {
		this.context = context;
		this.posted = posted;
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
	 * @return 內容
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context 內容
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return 發文時戳
	 */
	public Date getPosted() {
		return posted;
	}

	/**
	 * @param posted 發文時戳
	 */
	public void setPosted(Date posted) {
		this.posted = posted;
	}
}
