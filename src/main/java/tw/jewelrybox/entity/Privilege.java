package tw.jewelrybox.entity;

import javax.persistence.*;

/**
 * 權限
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Privilege.findAll", query = "SELECT p FROM Privilege p"),
	@NamedQuery(name = "Privilege.findAllAgents", query = "SELECT DISTINCT p.agent FROM Privilege p ORDER BY p.agent.id DESC"),
	@NamedQuery(name = "Privilege.findAllAgentsButMe", query = "SELECT DISTINCT p.agent FROM Privilege p WHERE p.agent <> :me ORDER BY p.agent.id DESC"),
	@NamedQuery(name = "Privilege.findOneByMappingAndAgent", query = "SELECT p FROM Privilege p WHERE p.mapping = :mapping AND p.agent = :agent")
})
@Table(catalog = "\"JewelryboxDB\"", schema = "\"public\"", name = "\"Privilege\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"mapping\"", "\"agent\""})
})
public class Privilege implements java.io.Serializable {

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
	 * 途徑
	 */
	@JoinColumn(name = "\"mapping\"", referencedColumnName = "\"id\"")
	@ManyToOne
	private Mapping mapping;

	/**
	 * 管理者
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
		if (!(object instanceof Privilege)) {
			return false;
		}
		Privilege other = (Privilege) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.jewelrybox.entity.Privilege[ id=" + id + " ]";
	}

	/**
	 * 預設建構子。
	 */
	public Privilege() {
	}

	/**
	 * @param id 主鍵(長度16位元)
	 */
	protected Privilege(Short id) {
		this.id = id;
	}

	/**
	 * @param agent 管理者
	 * @param mapping 途徑
	 */
	public Privilege(Mapping mapping, Someone agent) {
		this.mapping = mapping;
		this.agent = agent;
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
	 * @return 途徑
	 */
	public Mapping getMapping() {
		return mapping;
	}

	/**
	 * @param mapping 途徑
	 */
	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	/**
	 * @return 管理者
	 */
	public Someone getAgent() {
		return agent;
	}

	/**
	 * @param agent 管理者
	 */
	public void setAgent(Someone agent) {
		this.agent = agent;
	}
}
