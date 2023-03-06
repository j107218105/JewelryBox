package tw.jewelrybox.entity;

import java.util.Date;
import javax.persistence.*;

/**
 * 候選者
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Candidate.findAll", query = "SELECT c FROM Candidate c"),
	@NamedQuery(name = "Candidate.countByActivityAndRepliedTrueAndRepliedByNotNull", query = "SELECT COUNT(c.id) FROM Candidate c WHERE c.activity = :activity AND c.replied = TRUE AND c.repliedBy IS NOT NULL"),
	@NamedQuery(name = "Candidate.findBySomeoneOrderBySince", query = "SELECT a FROM Candidate c LEFT JOIN c.activity a WHERE c.someone = :someone ORDER BY a.since")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"Candidate\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"activity\"", "\"someone\""})
})
public class Candidate implements java.io.Serializable {

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
	 * 活動
	 */
	@JoinColumn(name = "\"activity\"", referencedColumnName = "\"id\"", nullable = false)
	@ManyToOne(optional = false)
	private Activity activity;

	/**
	 * 會員
	 */
	@JoinColumn(name = "\"someone\"", referencedColumnName = "\"id\"", nullable = false)
	@ManyToOne(optional = false)
	private Someone someone;

	/**
	 * 回覆
	 */
	@Column(name = "\"replied\"")
	private Boolean replied;

	/**
	 * 回覆時戳
	 */
	@Column(name = "\"repliedBy\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date repliedBy;

	/**
	 * 通知
	 */
	@Basic
	@Column(name = "\"notified\"")
	private Boolean notified;

	/**
	 * 證明(擷圖)
	 */
	@Column(name = "\"proof\"")
	@Lob
	private byte[] proof;

	/**
	 * 證明時戳
	 */
	@Column(name = "\"provedBy\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date provedBy;

	/**
	 * 會員
	 */
	@JoinColumn(name = "\"agent\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private Someone agent;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Candidate)) {
			return false;
		}
		Candidate other = (Candidate) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.Candidate[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public Candidate() {
	}

	/**
	 * @param id 主鍵(長度64位元)
	 */
	protected Candidate(Long id) {
		this.id = id;
	}

	/**
	 * @param activity 活動
	 * @param someone 會員
	 */
	public Candidate(Activity activity, Someone someone) {
		this.activity = activity;
		this.someone = someone;
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
	 * @return 回覆
	 */
	public Boolean getReplied() {
		return replied;
	}

	/**
	 * @param replied 回覆
	 */
	public void setReplied(Boolean replied) {
		this.replied = replied;
	}

	/**
	 * @return 回覆時戳
	 */
	public Date getRepliedBy() {
		return repliedBy;
	}

	/**
	 * @param repliedBy 回覆時戳
	 */
	public void setRepliedBy(Date repliedBy) {
		this.repliedBy = repliedBy;
	}

	/**
	 * @return 通知
	 */
	public Boolean getNotified() {
		return notified;
	}

	/**
	 * @param notified 通知
	 */
	public void setNotified(Boolean notified) {
		this.notified = notified;
	}

	/**
	 * @return 證明(擷圖)
	 */
	public byte[] getProof() {
		return proof;
	}

	/**
	 * @param proof 證明(擷圖)
	 */
	public void setProof(byte[] proof) {
		this.proof = proof;
	}

	/**
	 * @return 證明時戳
	 */
	public Date getProvedBy() {
		return provedBy;
	}

	/**
	 * @param provedBy 證明時戳
	 */
	public void setProvedBy(Date provedBy) {
		this.provedBy = provedBy;
	}

	/**
	 * @return 審核者
	 */
	public Someone getAgent() {
		return agent;
	}

	/**
	 * @param agent 審核者
	 */
	public void setAgent(Someone agent) {
		this.agent = agent;
	}

}
