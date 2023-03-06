package tw.jewelrybox.repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.*;

/**
 * 權限
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Short> {

	/**
	 * @param agent 管理者
	 * @return 該管理者所被允許進入的途徑數量。
	 */
	public long countByAgent(Someone agent);

	/**
	 * 開發期為了方便。
	 *
	 * @param pageable 分頁參數
	 * @return 管理者們。
	 */
	@Query(name = "Privilege.findAllAgents")
	public Page<Someone> findAllAgents(Pageable pageable);

	/**
	 * 正式發佈後使用。
	 *
	 * @param me 管理者
	 * @param pageable 分頁參數
	 * @return 除了自己以外的管理者們。
	 */
	@Query(name = "Privilege.findAllAgentsButMe")
	public Page<Someone> findAllAgentsButMe(@Param("me") Someone me, Pageable pageable);

	/**
	 * @param agent 管理者
	 * @return
	 */
	public Collection<Privilege> findByAgent(@Param("agent") Someone agent);

	/**
	 * @param agent 管理者
	 * @param pageable
	 * @return
	 */
	public Page<Privilege> findByAgent(@Param("agent") Someone agent, Pageable pageable);

	/**
	 * @param mapping 途徑
	 * @param agent 管理者
	 * @return 權限或空值。
	 */
	public Privilege findOneByMappingAndAgent(@Param("mapping") Mapping mapping, @Param("agent") Someone agent);
}
