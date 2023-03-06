package tw.jewelrybox.entity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 活動
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Activity.findAll", query = "SELECT a FROM Activity a")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"Activity\"")
public class Activity implements java.io.Serializable {

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
	 * 活動名稱
	 */
	@Basic(optional = false)
	@Column(name = "\"name\"", nullable = false, length = 2147483647)
	@NotNull
	@Size(min = 1, max = 2147483647)
	private String name;

	/**
	 * 人數
	 */
	@Basic(optional = false)
	@Column(name = "\"headcount\"", nullable = false)
	@NotNull
	private int headcount;

	/**
	 * 價值點數
	 */
	@Basic(optional = false)
	@Column(name = "\"score\"", nullable = false)
	@NotNull
	private short score;

	/**
	 * 開始時戳
	 */
	@Basic(optional = false)
	@Column(name = "\"since\"", nullable = false)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date since;

	/**
	 * 結束時戳
	 */
	@Basic(optional = false)
	@Column(name = "\"until\"", nullable = false)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date until;

	/**
	 * 已通知
	 */
	@Basic(optional = false)
	@Column(name = "\"notified\"", nullable = false)
	@NotNull
	private boolean notified;

	/**
	 * 年齡需求
	 */
	@Basic
	@Column(name = "\"age\"")
	private Short age;

	/**
	 * 性別需求
	 */
	@Basic
	@Column(name = "\"gender\"")
	private Boolean gender;

	/**
	 * 職業需求
	 */
	@JoinColumn(name = "\"occupation\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private Occupation occupation;

	/**
	 * 行政區需求
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
		if (!(object instanceof Activity)) {
			return false;
		}
		Activity other = (Activity) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.Activity[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public Activity() {
		this.notified = false;
	}

	/**
	 * @param id 主鍵(長度32位元)
	 */
	protected Activity(Integer id) {
		this.id = id;
	}

	/**
	 * @param name 活動名稱
	 * @param headcount 人數
	 * @param score 價值點數
	 * @param since 開始時戳
	 * @param until 結束時戳
	 */
	public Activity(String name, int headcount, short score, Date since, Date until) {
		this.name = name;
		this.headcount = headcount;
		this.score = score;
		this.since = since;
		this.until = until;
		this.notified = false;
	}

	/**
	 * @param name 活動名稱
	 * @param headcount 人數
	 * @param score 價值點數
	 * @param since 開始時戳
	 * @param until 結束時戳
	 * @param notified 已通知
	 */
	public Activity(String name, int headcount, short score, Date since, Date until, boolean notified) {
		this.name = name;
		this.headcount = headcount;
		this.score = score;
		this.since = since;
		this.until = until;
		this.notified = notified;
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
	 * @return 活動名稱
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 活動名稱
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 人數
	 */
	public int getHeadcount() {
		return headcount;
	}

	/**
	 * @param headcount 人數
	 */
	public void setHeadcount(int headcount) {
		this.headcount = headcount;
	}

	/**
	 * @return 價值點數
	 */
	public short getScore() {
		return score;
	}

	/**
	 * @param score 價值點數
	 */
	public void setScore(short score) {
		this.score = score;
	}

	/**
	 * @return 開始時戳
	 */
	public Date getSince() {
		return since;
	}

	/**
	 * @param since 開始時戳
	 */
	public void setSince(Date since) {
		this.since = since;
	}

	/**
	 * @return 結束時戳
	 */
	public Date getUntil() {
		return until;
	}

	/**
	 * @param until 結束時戳
	 */
	public void setUntil(Date until) {
		this.until = until;
	}

	/**
	 * @return 已通知
	 */
	public boolean isNotified() {
		return notified;
	}

	/**
	 * @param notified 已通知
	 */
	public void setNotified(boolean notified) {
		this.notified = notified;
	}

	/**
	 * @return 年齡需求
	 */
	public Short getAge() {
		return age;
	}

	/**
	 * @param age 年齡需求
	 */
	public void setAge(Short age) {
		this.age = age;
	}

	/**
	 * @return 性別需求
	 */
	public Boolean getGender() {
		return gender;
	}

	/**
	 * @param gender 性別需求
	 */
	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	/**
	 * @return 職業需求
	 */
	public Occupation getOccupation() {
		return occupation;
	}

	/**
	 * @param occupation 職業需求
	 */
	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
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
